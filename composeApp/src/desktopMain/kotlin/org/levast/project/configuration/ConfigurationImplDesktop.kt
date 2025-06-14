package org.levast.project.configuration

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.File


class ConfigurationImplDesktop() : IConfiguration {

    private val PROPERTY_FILE_PATH ="properties"

    private var properties:AppProperties = AppProperties()

    override fun getEndpointServer() = "https://${properties.ipAdressServer}:${properties.portServer}"

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
}