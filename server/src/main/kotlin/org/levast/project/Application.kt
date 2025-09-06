package org.levast.project

import ApiableItem
import Arme
import Armure
import Bouclier
import ENDPOINT_COMPTE_UTILISATEUR_DELETE
import ENDPOINT_COMPTE_UTILISATEUR_GET_ALL
import ENDPOINT_COMPTE_UTILISATEUR_INSERT
import ENDPOINT_COMPTE_UTILISATEUR_ROOT
import ENDPOINT_COMPTE_UTILISATEUR_UPDATE
import ENDPOINT_MAJ_CARACS_JOUEUR
import ENDPOINT_MAJ_NOTES_JOUEUR
import ENDPOINT_RECHERCHE_STRICTE
import Equipe
import Joueur
import Monster
import QUERY_PARAMETER_ID
import QUERY_PARAMETER_NOM
import Sort
import Special
import canUserModifyJoueur
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import checkIfPasswordIsCorrectForUser
import collectionsApiableItem
import com.mongodb.MongoBulkWriteException
import deleteCompteUtilisateur
import getAllComptesUtilisateurs
import initDatabase
import getCollectionElements
import getCollectionElementsArraysAsString
import getCollectionElementsAsString
import insertCompteUtilisateur
import insertListElements
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.principal
import io.ktor.server.engine.*
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.KtorSimpleLogger
import isUserAdmin
import org.levast.project.model.CompteUtilisateur
import org.levast.project.network.AnythingItemDTO
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import org.slf4j.LoggerFactory
import updateCompteUtilisateur
import java.io.File
import java.io.FileNotFoundException

val unmutableListApiItemDefinition =
    listOf(Arme(), Armure(), Monster(), Bouclier(), Sort(), Special(), Joueur(), Equipe())

val logger = KtorSimpleLogger("logger")

fun main() {


    (LoggerFactory.getILoggerFactory() as LoggerContext).getLogger("org.mongodb.driver").level =
        Level.ERROR

    initDatabase()


    embeddedServer(Netty, port = SERVER_KTOR_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = org.slf4j.event.Level.WARN

    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowSameOrigin
    }
    install(Compression) {
        gzip()
    }
    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                if (checkIfPasswordIsCorrectForUser(credentials.name, credentials.password)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
    routing {
        get("/") {
            call.respondText(
                this::class.java.classLoader?.getResource("index.html")!!.readText(),
                ContentType.Text.Html
            )
        }
        static("/") {
            resources("")
        }

        route("/all") {

            put {
                val listNameElementsSearched = call.receive<List<String>>()
                val listItemsFound = mutableListOf<AnythingItemDTO>()

                //FIXME quand il y'a plus de 100 elements ça ralentit fortement selon la doc
                    for (tableObject in unmutableListApiItemDefinition) {
                        getCollectionElementsArraysAsString(
                            tableObject,
                            listNameElementsSearched,
                            true
                        ).map {
                            AnythingItemDTO(
                                tableObject.nameForApi,
                                it
                            )
                        }.let {
                            if (it.isNotEmpty()) {
                                listItemsFound.addAll(it)
                            }
                    }
                }
                call.respond(listItemsFound.ifEmpty { HttpStatusCode.NoContent })


            }
            get {
                val nom = call.request.queryParameters[QUERY_PARAMETER_NOM] ?: ""
                val rechercheStricte: Boolean =
                    call.request.queryParameters[ENDPOINT_RECHERCHE_STRICTE] == "true"
                val listItemsFound = mutableListOf<AnythingItemDTO>()
                //Pour chaque element on regarde s'il y'en a un qui matche le nom demandé
                for (tableObject in unmutableListApiItemDefinition) {
                    getCollectionElementsAsString(
                        tableObject,
                        nom,
                        rechercheStricte
                    ).map { AnythingItemDTO(tableObject.nameForApi, it) }.let {
                        if (it.isNotEmpty()) {
                            listItemsFound.addAll(it)
                        }
                    }
                }
                call.respond(listItemsFound.ifEmpty { HttpStatusCode.NoContent })
            }
        }
        unmutableListApiItemDefinition.forEach { itapiable ->
            route("/" + itapiable.nameForApi!!) {
                get {
                    val nom = call.request.queryParameters[QUERY_PARAMETER_NOM] ?: ""
                    val itemsFound = getCollectionElements(itapiable, nom)
                    call.respond(itemsFound.ifEmpty { HttpStatusCode.NoContent })
                }
                get("/$ENDPOINT_RECHERCHE_STRICTE") {
                    val nom = call.request.queryParameters[QUERY_PARAMETER_NOM] ?: ""
                    val itemsFound = getCollectionElements(itapiable, nom, true)
                    call.respond(itemsFound.ifEmpty { HttpStatusCode.NoContent })
                }
                get("/" + itapiable.uploadFileForApi) {
                    //retrieve the data from csv file

                    val parsedData: List<ApiableItem> = try {
                        itapiable.decomposeCSV(
                            File("${itapiable.nameForApi}.csv").readLines()
                                .asSequence()
                        )
                    } catch (e: FileNotFoundException) {
                        //si le fichier existe pas on retourne une liste vide
                        logger.error(e.stackTraceToString())
                        listOf()
                    }
                    //send data to database
                    try {
                        insertListElements(itapiable, parsedData)
                    } catch (e: MongoBulkWriteException) {
                        logger.error(e.stackTraceToString())
                    }
                    call.respond(parsedData)
                }
                authenticate("auth-basic") {

                    post("/" + itapiable.updateForApi) {
                        logger.debug("post en cours")

                        if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {
                            val elementToUpdate: ApiableItem = call.receive()

                            val resInsert =
                                collectionsApiableItem[itapiable.nameForApi]!!.updateOneById(
                                    elementToUpdate._id,
                                    elementToUpdate
                                )

                            if (resInsert.modifiedCount > 0) {
                                call.respond(HttpStatusCode.OK)
                            } else {
                                call.respond(HttpStatusCode.ExpectationFailed)
                            }
                        } else {
                            call.respond(
                                HttpStatusCode.Forbidden,
                                "Vous n'avez pas le role admin et il le faut pour MAJ des items"
                            )
                        }


                    }
                    post("/" + itapiable.insertForApi) {

                        if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {
                            logger.debug("insert en cours")
                            val elementToInsert: ApiableItem = call.receive()

                            //S'il y'a déjà un élément avec cet identifiant là, on insère pas, faut supprimer avant
                            val resInsert =
                                if (collectionsApiableItem[itapiable.nameForApi]!!.countDocuments(
                                        ApiableItem::_id eq elementToInsert._id
                                    ) < 1
                                ) {
                                    collectionsApiableItem[itapiable.nameForApi]!!.insertMany(
                                        listOf(
                                            elementToInsert
                                        ) as List<Nothing>
                                    )
                                } else {
                                    null
                                }

                            if (resInsert?.wasAcknowledged() == true) {
                                call.respond(HttpStatusCode.OK)
                            } else {
                                call.respond(HttpStatusCode.ExpectationFailed)
                            }
                        } else {
                            call.respond(
                                HttpStatusCode.Forbidden,
                                "Vous n'avez pas le role admin et il le faut pour créer des items"
                            )
                        }

                    }
                    post("/" + itapiable.deleteForApi) {
                        if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {

                            val nom = call.request.queryParameters[QUERY_PARAMETER_NOM] ?: ""
                            if (collectionsApiableItem[itapiable.nameForApi]!!.deleteOne(
                                    ApiableItem::nom eq nom
                                )
                                    .wasAcknowledged()
                            ) {
                                call.respond(HttpStatusCode.OK)
                            } else {
                                call.respond(HttpStatusCode.ExpectationFailed)
                            }
                        } else {
                            call.respond(
                                HttpStatusCode.Forbidden,
                                "Vous n'avez pas le role admin et il le faut pour supprimer des items"
                            )
                        }
                    }
                }
                get("/" + itapiable.downloadForApi) {
                    val itemsFound = getCollectionElements(itapiable, ".*")
                    val stringFileCSV = itemsFound.first().getParsingRulesAttributesAsList()
                        .joinToString(";") { it.split(":").first() } + "\n" +
                            itemsFound.map { it.getDeparsedAttributes().joinToString(";") }
                                .joinToString("\n")
                    val filename = "${itapiable.nameForApi}.csv"
                    val file = File(filename)
                    file.writeText(stringFileCSV)
                    call.response.header(
                        HttpHeaders.ContentDisposition,
                        ContentDisposition.Attachment.withParameter(
                            ContentDisposition.Parameters.FileName,
                            filename
                        )
                            .toString()
                    )
                    call.respondFile(file)
                }
                if (itapiable is Joueur) {
                    authenticate("auth-basic") {

                        post("/$ENDPOINT_MAJ_CARACS_JOUEUR") {

                            val joueurToUpdateCaracs: Joueur =
                                getApiableElementAccordingToType(call, itapiable) as Joueur

                            val username = call.principal<UserIdPrincipal>()?.name
                            if (canUserModifyJoueur(username, joueurToUpdateCaracs.nom)) {
                                val resInsertCaracs =
                                    collectionsApiableItem[itapiable.nameForApi]!!.updateOne(
                                        filter = Joueur::_id eq joueurToUpdateCaracs._id,
                                        update = setValue(
                                            Joueur::caracActuel,
                                            joueurToUpdateCaracs.caracActuel
                                        )
                                    )
                                val resInsertDetails =
                                    collectionsApiableItem[itapiable.nameForApi]!!.updateOne(
                                        filter = Joueur::_id eq joueurToUpdateCaracs._id,
                                        update = setValue(
                                            Joueur::details,
                                            joueurToUpdateCaracs.details
                                        )
                                    )
                                val resInsertEquipped =
                                    collectionsApiableItem[itapiable.nameForApi]!!.updateOne(
                                        filter = Joueur::_id eq joueurToUpdateCaracs._id,
                                        update = setValue(
                                            Joueur::chaineEquipementSelectionneSerialisee,
                                            joueurToUpdateCaracs.chaineEquipementSelectionneSerialisee
                                        )
                                    )
                                val resInsertUtilisations =
                                    collectionsApiableItem[itapiable.nameForApi]!!.updateOne(
                                        filter = Joueur::_id eq joueurToUpdateCaracs._id,
                                        update = setValue(
                                            Joueur::utilisationsRestantesItem,
                                            joueurToUpdateCaracs.utilisationsRestantesItem
                                        )
                                    )


                                if (resInsertCaracs.wasAcknowledged() && resInsertDetails.wasAcknowledged() && resInsertEquipped.wasAcknowledged() && resInsertUtilisations.wasAcknowledged()) {
                                    call.respond(HttpStatusCode.OK)
                                } else if (resInsertCaracs.wasAcknowledged() || resInsertDetails.wasAcknowledged() || resInsertEquipped.wasAcknowledged() || resInsertUtilisations.wasAcknowledged()) {
                                    //dans le cas où seulement une des deux données a correctement etait mise à jour
                                    call.respond(HttpStatusCode.PartialContent)
                                } else {
                                    call.respond(HttpStatusCode.ExpectationFailed)
                                }
                            } else {
                                call.respond(
                                    HttpStatusCode.Forbidden,
                                    "Vous , ${username}, n'avez pas le droit de modifier ${joueurToUpdateCaracs.nom}"
                                )
                            }
                        }
                        post("/$ENDPOINT_MAJ_NOTES_JOUEUR") {
                            val username = call.principal<UserIdPrincipal>()?.name

                            val joueurToUpdateNotes: Joueur =
                                getApiableElementAccordingToType(call, itapiable) as Joueur

                            if (canUserModifyJoueur(username, joueurToUpdateNotes.nom)) {

                                val resUpdateNotesPnj =
                                    collectionsApiableItem[itapiable.nameForApi]!!.updateOne(
                                        filter = Joueur::_id eq joueurToUpdateNotes._id,
                                        update = setValue(
                                            Joueur::notesPnj,
                                            joueurToUpdateNotes.notesPnj
                                        )
                                    )

                                if (resUpdateNotesPnj.wasAcknowledged()) {
                                    call.respond(HttpStatusCode.OK)
                                } else if (resUpdateNotesPnj.wasAcknowledged()) {
                                    //dans le cas où seulement une des deux données a correctement etait mise à jour
                                    call.respond(HttpStatusCode.PartialContent)
                                } else {
                                    call.respond(HttpStatusCode.ExpectationFailed)
                                }
                            } else {
                                call.respond(
                                    HttpStatusCode.Forbidden,
                                    "Vous , ${username}, n'avez pas le droit de modifier les notes de ${joueurToUpdateNotes.nom}"
                                )
                            }
                        }
                    }
                }
            }
        }
        //Gestion des comptes utilisateurs
        route("/$ENDPOINT_COMPTE_UTILISATEUR_ROOT") {
            authenticate("auth-basic") {

                get("/$ENDPOINT_COMPTE_UTILISATEUR_GET_ALL") {
                    if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {
                        val itemsFound = getAllComptesUtilisateurs()
                        call.respond(itemsFound.ifEmpty { HttpStatusCode.NoContent })
                    } else {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            "Vous n etes pas admin vous ne pouvez pas recuperer tous les comptes"
                        )
                    }
                }

                post("/$ENDPOINT_COMPTE_UTILISATEUR_UPDATE") {

                    if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {
                        val compteToUpdate: CompteUtilisateur = call.receive()

                        call.respond(updateCompteUtilisateur(compteToUpdate))
                    } else {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            "Vous n etes pas admin vous ne pouvez pas mettre a jour un profil"
                        )
                    }


                }
                post("/$ENDPOINT_COMPTE_UTILISATEUR_INSERT") {

                    if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {
                        val compteToInsert: CompteUtilisateur = call.receive()

                        try {
                            call.respond(insertCompteUtilisateur(compteToInsert))
                        } catch (e: Exception) {
                            call.respond(
                                HttpStatusCode.ExpectationFailed,
                                "impossible d'inserer le nouveau profil"
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            "Vous n etes pas admin vous ne pouvez pas creer un profil"
                        )
                    }


                }
                delete("/$ENDPOINT_COMPTE_UTILISATEUR_DELETE") {

                    if (isUserAdmin(call.principal<UserIdPrincipal>()?.name)) {
                        val idUtilisateur =
                            call.request.queryParameters[QUERY_PARAMETER_ID]?.toInt()

                        try {
                            call.respond(deleteCompteUtilisateur(idUtilisateur))
                        } catch (e: Exception) {
                            call.respond(
                                HttpStatusCode.ExpectationFailed,
                                "impossible de supprimer le nouveau profil"
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            "Vous n etes pas admin vous ne pouvez pas supprimer un profil"
                        )
                    }


                }
            }
        }
    }
}


/**
 * This function return the object deducing his type
 */
private suspend fun getApiableElementAccordingToType(
    call: ApplicationCall,
    itapiable: ApiableItem
) = when (itapiable) {
    //TODO ajouter une ligne dans le when quand on ajoute un table dans la bdd

    is Arme -> {
        call.receive<Arme>()
    }

    is Monster -> {
        call.receive<Monster>()
    }

    is Armure -> {
        call.receive<Armure>()
    }

    is Bouclier -> {
        call.receive<Bouclier>()
    }

    is Sort -> {
        call.receive<Sort>()
    }

    is Special -> {
        call.receive<Special>()
    }

    is Joueur -> {
        call.receive<Joueur>()
    }

    is Equipe -> {
        call.receive<Equipe>()
    }

    else -> {
        call.receive<Armure>()
    }
}
