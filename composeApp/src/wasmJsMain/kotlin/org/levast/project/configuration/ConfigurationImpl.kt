package org.levast.project.configuration

import org.levast.project.SERVER_KTOR_PORT


// At the top level of your kotlin file:

class ConfigurationImpl() : IConfiguration {

    private var properties: AppProperties = AppProperties("localhost","",)

    override fun getEndpointServer() = "http://${properties.ipAdressServer}:${properties.portServer}"

    override fun getIpAdressTargetServer() =  properties.ipAdressServer

    override fun setIpAdressTargetServer(adresseIp: String) {
        properties.ipAdressServer=adresseIp
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean) {
        properties.isUserMode = isUserMode
    }

    override fun getMode(): Boolean = properties.isUserMode ?: true
}