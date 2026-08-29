package org.levast.project.network

import ApiableItem
import Arme
import Armure
import Bouclier
import ClasseType
import ENDPOINT_COMPTE_UTILISATEUR_DELETE
import ENDPOINT_COMPTE_UTILISATEUR_GET_ALL
import ENDPOINT_COMPTE_UTILISATEUR_INSERT
import ENDPOINT_COMPTE_UTILISATEUR_ROOT
import ENDPOINT_COMPTE_UTILISATEUR_UPDATE
import ENDPOINT_MAJ_CARACS_JOUEUR
import ENDPOINT_MAJ_NOTES_JOUEUR
import ENDPOINT_RECHERCHE_STRICTE
import ENDPOINT_RECHERCHE_TOUT
import Equipe
import IListItem
import Joueur
import Monster
import QUERY_PARAMETER_ID
import QUERY_PARAMETER_NOM
import Race
import Sort
import Special
import cleanupForDB
import org.levast.project.configuration.IConfiguration
import extractDecouvertesListFromEquipe
import extractEquipementsListFromJoueur
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.levast.project.ERROR_NETWORK_MESSAGE
import org.levast.project.model.CompteUtilisateur
import org.levast.project.repository.NotificationRepository
import unmutableListApiItemDefinition


class ApiApp(val config: IConfiguration, val notification: NotificationRepository) {

    val endpoint get() = config.getEndpointServer()

    var jsonClient: HttpClient;

    init {
        jsonClient = createHttpClient()
    }

    fun initJsonClient() {
        jsonClient.close() //on cloture le client http et on en recrée un nouveau
        jsonClient = createHttpClient();
    }

    private fun createHttpClient(): HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 50000
        }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        username = config.getUserAuthentication()?.userName ?: "",
                        password = config.getUserAuthentication()?.password ?: ""
                    )
                }
                realm = "Access to the '/' path"
                sendWithoutRequest { request ->
                    val method = request.method

                    method == HttpMethod.Post || method == HttpMethod.Delete || method == HttpMethod.Patch
                }
            }
        }
    }

    private suspend fun searchAnythingStringEncoded(
        nomSearched: String,
        strict: Boolean
    ): List<AnythingItemDTO> {
        return catchNetworkError("Recherche ${if(strict)"stricte" else ""} $nomSearched OK") {
            jsonClient.get("$endpoint/$ENDPOINT_RECHERCHE_TOUT") {
                url {
                    parameters.append(ENDPOINT_RECHERCHE_STRICTE, strict.toString())
                    parameters.append(QUERY_PARAMETER_NOM, nomSearched)
                }
            }
        }?.let {
            if (it.status != HttpStatusCode.NoContent) it.body<List<AnythingItemDTO>>() else listOf()
        } ?: listOf()
    }

    private suspend fun searchEverything(
        searchedNames: List<String>,
    ): List<IListItem> {
        return deserializeAnythingItemDTO(searchEverythingStringEncoded(searchedNames))
    }

    suspend fun searchAnything(nomSearched: String, strict: Boolean = false): List<IListItem> {
        return deserializeAnythingItemDTO(searchAnythingStringEncoded(nomSearched, strict))
    }

    private fun deserializeAnythingItemDTO(listAnythingItem: List<AnythingItemDTO>): List<IListItem> {
        val listItemsFound = mutableListOf<IListItem>()
        for (anythingItem in listAnythingItem) {

            if (anythingItem.itemContent != null && anythingItem.typeItem != null) {
                // Créer une instance de la classe
                val itemClasseReify: ApiableItem? =
                    unmutableListApiItemDefinition.find { it.nameForApi == anythingItem.typeItem }

                //TODO ajouter ici les nouvelles tables a deserialiser
                listItemsFound.add(
                    when (itemClasseReify) {
                        is Arme -> Json.decodeFromString<Arme>(anythingItem.itemContent!!)
                        is Armure -> Json.decodeFromString<Armure>(anythingItem.itemContent!!)
                        is Monster -> Json.decodeFromString<Monster>(anythingItem.itemContent!!)
                        is Bouclier -> Json.decodeFromString<Bouclier>(anythingItem.itemContent!!)
                        is Sort -> Json.decodeFromString<Sort>(anythingItem.itemContent!!)
                        is Special -> Json.decodeFromString<Special>(anythingItem.itemContent!!)
                        is Joueur -> Json.decodeFromString<Joueur>(anythingItem.itemContent!!)
                        is Equipe -> Json.decodeFromString<Equipe>(anythingItem.itemContent!!)
                        is Race -> Json.decodeFromString<Race>(anythingItem.itemContent!!)
                        is ClasseType -> Json.decodeFromString<ClasseType>(anythingItem.itemContent!!)
                        else -> throw IllegalArgumentException("Impossible de deserialiser l'objet json recu, il ne fait pas parti des elements connus")
                    }
                )
            }
        }
        return listItemsFound
    }

    private suspend fun searchEverythingStringEncoded(searchedNames: List<String>): List<AnythingItemDTO> {
        return catchNetworkError("Recherche de tous les items OK") {
            jsonClient.put("$endpoint/$ENDPOINT_RECHERCHE_TOUT") {
                contentType(ContentType.Application.Json)
                setBody(searchedNames)
            }
        }?.let {
            if (it.status != HttpStatusCode.NoContent) it.body<List<AnythingItemDTO>>() else listOf()
        } ?: listOf()
    }

    suspend fun searchJoueur(nomSearched: String): List<Joueur>? {
        return catchNetworkError("Recherche joueur $nomSearched OK") {
            jsonClient.get(endpoint + "/" + Joueur().nameForApi) {
                url {
                    parameters.append(QUERY_PARAMETER_NOM, nomSearched)
                }
            }
        }?.let {
            if (it.status != HttpStatusCode.NoContent) it.body<List<Joueur>>() else null
        }
    }

    suspend inline fun <reified T : ApiableItem> searchSomethings(
        blankItemToSearchApi: T,
        nomSearched: String
    ): List<T>? {
        return catchNetworkError("Recherche de quoi que ce soit $nomSearched OK") {
            jsonClient.get(endpoint + "/" + blankItemToSearchApi.nameForApi) {
                url {
                    parameters.append(QUERY_PARAMETER_NOM, nomSearched)
                }
            }
        }?.let {
            if (it.status != HttpStatusCode.NoContent) it.body<List<T>>() else null
        }
    }

    suspend fun searchAllJoueur(listNomSearched: List<String>): List<Joueur> {
        val listJoueurs = mutableListOf<Joueur>()
        listNomSearched.forEach { nameSearched ->
            if (nameSearched.isNotBlank()) {
                //pour chacun des équipements on cherche dans chacune des tables mais on recupere que le premier trouvé
                searchJoueur(nameSearched)?.let { joueurTrouves ->
                    if (joueurTrouves.isNotEmpty()) listJoueurs.addAll(joueurTrouves)
                }
            }
        }
        return listJoueurs
    }

    suspend fun searchEquipe(nomSearched: String): List<Equipe>? {
        return catchNetworkError("Recherche de l'équipe $nomSearched OK") {
            jsonClient.get(endpoint + "/" + Equipe().nameForApi) {
                url {
                    parameters.append(QUERY_PARAMETER_NOM, nomSearched)
                }
            }
        }?.let {
            if (it.status != HttpStatusCode.NoContent) it.body<List<Equipe>>() else null
        }
    }

    suspend fun searchAllEquipementJoueur(joueur: Joueur): List<IListItem> {
        val listEquipements = mutableListOf<IListItem>()
        extractEquipementsListFromJoueur(joueur).let {
            if (it.isNotEmpty()) {
                listEquipements.addAll(searchEverything(it))
            }
        }
        return listEquipements
    }

    suspend fun searchAllDecouvertesEquipe(equipe: Equipe): List<IListItem> {
        var listDecouvertes = mutableListOf<IListItem>()
        extractDecouvertesListFromEquipe(equipe).let {
            if (it.isNotEmpty()) {
                //pour chacun des équipements on cherche dans chacune des tables mais on recupere que le premier trouvé
                listDecouvertes.addAll(searchEverything(it))
            }
        }
        return listDecouvertes
    }


    /**
     * pour mettre à jour les stats d'un joueur
     */
    //Ne mets à jour que les notes du joueurs
    suspend fun updateNotesPnjJoueur(joueurToUpdate: Joueur): Boolean {
        return catchNetworkError("Maj notes ${joueurToUpdate.nom} OK") {
            jsonClient.post(endpoint + "/" + joueurToUpdate.nameForApi + "/${ENDPOINT_MAJ_NOTES_JOUEUR}") {
                contentType(ContentType.Application.Json)
                setBody(joueurToUpdate)
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    //Mets à jour les stats du joueurs
    suspend fun updateJoueur(joueurToUpdate: Joueur): Boolean {
        return catchNetworkError("Maj stats ${joueurToUpdate.nom} OK") {
            jsonClient.post(endpoint + "/" + joueurToUpdate.nameForApi + "/$ENDPOINT_MAJ_CARACS_JOUEUR") {
                contentType(ContentType.Application.Json)
                setBody(joueurToUpdate)
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    suspend fun insertItem(itemSelected: ApiableItem): Boolean {
        return catchNetworkError("Insertion ${itemSelected.nom} OK") {
            jsonClient.post(endpoint + "/" + itemSelected.nameForApi + "/${itemSelected.insertForApi}") {
                contentType(ContentType.Application.Json)
                setBody(itemSelected)
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    suspend fun updateItem(itemSelected: ApiableItem): Boolean {
        return catchNetworkError("Mise à jour ${itemSelected.nom} OK") {
            jsonClient.post(endpoint + "/" + itemSelected.nameForApi + "/${itemSelected.updateForApi}") {
                contentType(ContentType.Application.Json)
                setBody(itemSelected)
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    suspend fun deleteItem(itemSelected: ApiableItem): Boolean {
        return catchNetworkError("Suppression ${itemSelected.nom} OK") {
            jsonClient.post(endpoint + "/" + itemSelected.nameForApi + "/${itemSelected.deleteForApi}") {
                url {
                    parameters.append(QUERY_PARAMETER_NOM, itemSelected.nom)
                }
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    //Gérer les comptes utilisateurs
    suspend fun getAllCompteUtilisateur(): List<CompteUtilisateur> {
        return catchNetworkError("Recherche de tous les comptes OK") {
            jsonClient.get("$endpoint/$ENDPOINT_COMPTE_UTILISATEUR_ROOT/$ENDPOINT_COMPTE_UTILISATEUR_GET_ALL") {
            }
        }?.let {
            if (it.status == HttpStatusCode.OK) it.body<List<CompteUtilisateur>>() else listOf()
        } ?: listOf()
    }

    //Gérer les comptes utilisateurs
    suspend fun insertCompteUtilisateur(compteUtilisateur: CompteUtilisateur): Boolean {
        return catchNetworkError("Insertion compte ${compteUtilisateur.nom} OK") {
            jsonClient.post("$endpoint/$ENDPOINT_COMPTE_UTILISATEUR_ROOT/$ENDPOINT_COMPTE_UTILISATEUR_INSERT") {
                contentType(ContentType.Application.Json)
                setBody(compteUtilisateur)
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    suspend fun updateCompteUtilisateur(compteUtilisateur: CompteUtilisateur): Boolean {
        return catchNetworkError("Mise à jour compte ${compteUtilisateur.nom} OK") {
            jsonClient.post("$endpoint/$ENDPOINT_COMPTE_UTILISATEUR_ROOT/$ENDPOINT_COMPTE_UTILISATEUR_UPDATE") {
                contentType(ContentType.Application.Json)
                setBody(compteUtilisateur)
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }

    suspend fun deleteCompteUtilisateur(compteUtilisateur: CompteUtilisateur): Boolean {
        return catchNetworkError("Suppression compte ${compteUtilisateur.nom} OK") {
            jsonClient.delete("$endpoint/$ENDPOINT_COMPTE_UTILISATEUR_ROOT/$ENDPOINT_COMPTE_UTILISATEUR_DELETE") {
                url {
                    parameters.append(QUERY_PARAMETER_ID, compteUtilisateur.id.toString())
                }
            }
        }?.let {
            it.status == HttpStatusCode.OK
        } == true
    }


    suspend fun catchNetworkError(
        messageOk : String = "Requête OK",
        errorMessage: String = ERROR_NETWORK_MESSAGE,
        networkAction: suspend () -> HttpResponse,
    ): HttpResponse? {
        return try {
            networkAction().also {
                when(it.status){
                    HttpStatusCode.Unauthorized -> notification.sendNotification("Erreur  d'authentification ${it.status}")
                    HttpStatusCode.Forbidden -> notification.sendNotification("Erreur d'autorisation ${it.status}")
                    HttpStatusCode.NotFound -> notification.sendNotification("Erreur de recherche ${it.status}")
                    HttpStatusCode.InternalServerError -> notification.sendNotification("Erreur serveur ${it.status}")
                    HttpStatusCode.BadRequest -> notification.sendNotification("Erreur de formatage ${it.status}")
                    HttpStatusCode.Conflict -> notification.sendNotification("Erreur de conflit ${it.status}")
                    HttpStatusCode.UnprocessableEntity -> notification.sendNotification("Erreur de traitement ${it.status}")
                    HttpStatusCode.TooManyRequests -> notification.sendNotification("Erreur trop de requetes ${it.status}")
                    HttpStatusCode.RequestTimeout -> notification.sendNotification("Erreur de timeout ${it.status}")
                    HttpStatusCode.GatewayTimeout -> notification.sendNotification("Erreur de timeout ${it.status}")
                    HttpStatusCode.ServiceUnavailable -> notification.sendNotification("Erreur de service indisponible ${it.status}")
                    HttpStatusCode.OK -> notification.sendNotification(messageOk)
                }
            }
        } catch (e: Exception) {
            println(
                " $errorMessage\n " +
                        e.stackTraceToString()
            )
            return null
        }
    }

    private fun <T> catchNetworkErrorUnsuspendly(
        errorMessage: String = ERROR_NETWORK_MESSAGE,
        defaultReturnValue: T,
        networkAction: () -> T,
    ): T {
        return try {
            networkAction()
        } catch (e: Exception) {
            println(
                " $errorMessage\n " +
                        e.stackTraceToString()
            )
            return defaultReturnValue
        }
    }

    fun createUrlImageFromItem(item: IListItem) =
        endpoint + "/images/" + item.nom.cleanupForDB().replace(" ", "") + ".jpg"
}




