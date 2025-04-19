package org.levast.project.configuration

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.File


class ConfigurationImplDesktop() : IConfiguration {

    private val PROPERTY_FILE_PATH ="properties"

    private var properties:AppProperties = AppProperties()

    override fun getEndpointServer() = "http://${properties.ipAdressServer}:${properties.portServer}"

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
                        println(e.stackTraceToString())
                        saveToFile()//on cree le fichier
                        AppProperties("localhost")
                    }

            }
        }
    }

    override fun getIpAdressTargetServer() =  properties.ipAdressServer

    override fun setIpAdressTargetServer(adresseIp: String) {
        properties.ipAdressServer=adresseIp
        saveToFile()
    }

    private fun saveToFile() {

        File(PROPERTY_FILE_PATH).writeText(Json.encodeToString(properties))
    }

    /*
    Sur Desktop y'a pas d'utilisateur associé à l'application donc on laisse vide ces fonctions
     */
    override fun setUserName(nomUser: String) {
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode=isUserMode
        saveToFile()
    }

    override fun getMode(): Boolean? = properties.isUserMode
}