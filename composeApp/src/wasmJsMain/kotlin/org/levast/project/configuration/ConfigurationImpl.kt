package org.levast.project.configuration

import org.levast.project.SERVER_KTOR_PORT_SSL


// At the top level of your kotlin file:

class ConfigurationImpl() : IConfiguration {

    private var properties: AppProperties = AppProperties("lesfeusperegrins.click")

    override fun getEndpointServer() = "${properties.getProtocol()}://${properties.adressServer}:${properties.getPortServer()}"

    override fun getAdressTargetServer() =  properties.adressServer

    override fun setadressTargetServer(adresseIp: String) {
        properties.adressServer=adresseIp
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser
    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode = isUserMode
    }

    override fun getMode(): Boolean? = properties.isUserMode

    override fun setHttpsMode(isHttpsOn: Boolean) {
        properties.isHttpsOn = isHttpsOn
    }

    override fun getIsHttpsOn() = properties.isHttpsOn

    override fun setUserAuthentication(userAuthentication: UserAuthentication) {
        properties.userAuthentication = userAuthentication
    }

    override fun getUserAuthentication(): UserAuthentication? = properties.userAuthentication
}