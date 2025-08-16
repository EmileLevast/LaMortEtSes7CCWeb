package org.levast.project.configuration

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.levast.project.DNS_ADRESS_SERVER
import java.io.File


class ConfigurationImplDesktop() : IConfiguration {

    private val PROPERTY_FILE_PATH ="properties"

    private var properties:AppProperties = AppProperties()

    override fun getEndpointServer() = "${properties.getProtocol()}://${properties.adressServer}:${properties.getPortServer()}"

    init {
        loadFileProperties()
    }

    private fun loadFileProperties(){
        runBlocking {
            coroutineScope {
                properties =
                    try {
                        Json.decodeFromString<AppProperties>(File(PROPERTY_FILE_PATH).readText())
                    } catch (e: Exception) {
                        println(e.stackTraceToString() + "\n TOUT va bien faut se détendre c'est moi qui affiche l'erreur")
                        saveToFile()//on cree le fichier
                        AppProperties(DNS_ADRESS_SERVER)
                    }

            }
        }
    }

    override fun getAdressTargetServer() =  properties.adressServer

    override fun setadressTargetServer(adresseServer: String) {
        properties.adressServer=adresseServer
        saveToFile()
    }

    private fun saveToFile() {

        File(PROPERTY_FILE_PATH).writeText(Json.encodeToString(properties))
    }


    override fun setUserName(nomUser: String) {
        properties.userName=nomUser
        saveToFile()
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode=isUserMode
        saveToFile()
    }

    override fun getMode(): Boolean? = properties.isUserMode

    override fun setHttpsMode(isHttpsOn: Boolean) {
        properties.isHttpsOn=isHttpsOn
        saveToFile()
    }

    override fun getIsHttpsOn() = properties.isHttpsOn

    override fun setUserAuthentication(userAuthentication: UserAuthentication){
        properties.userAuthentication=userAuthentication
        saveToFile()
    }
    override fun getUserAuthentication(): UserAuthentication? = properties.userAuthentication
}