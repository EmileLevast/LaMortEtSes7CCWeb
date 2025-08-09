package org.levast.project.configuration

import org.levast.project.SERVER_KTOR_PORT_SSL


// At the top level of your kotlin file:

class ConfigurationImpl() : IConfiguration {

    // TODO: Vérifier si cette adresse IP par défaut est toujours pertinente pour les environnements de test
    private var properties: AppProperties = AppProperties("lesfeusperegrins.click", portServer = SERVER_KTOR_PORT_SSL)

    override fun getEndpointServer() = "https://${properties.ipAdressServer}:${properties.portServer}"

    override fun getIpAdressTargetServer() =  properties.ipAdressServer

    override fun setIpAdressTargetServer(adresseIp: String) {
        properties.ipAdressServer=adresseIp
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode = isUserMode
    }

    override fun getMode(): Boolean? = properties.isUserMode
}