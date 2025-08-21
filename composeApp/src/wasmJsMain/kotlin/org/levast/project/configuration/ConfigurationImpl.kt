package org.levast.project.configuration

import kotlinx.browser.localStorage
import kotlinx.serialization.json.Json
import org.levast.project.SERVER_KTOR_PORT_SSL
import org.w3c.dom.set


// At the top level of your kotlin file:

class ConfigurationImpl() : IConfiguration {

    private var properties: AppProperties = AppProperties(
        localStorage.getItem(KEY_IP_ADDRESS) ?: "lesfeusperegrins.click",
        localStorage.getItem(KEY_USER_NAME) ?: "",
        localStorage.getItem(KEY_MODE)?.toBoolean(),
        localStorage.getItem(KEY_HTTPS_MODE)?.toBoolean() != false,
        localStorage.getItem(KEY_USER_AUTH)?.let { Json.decodeFromString(it) }
    )

    override fun getEndpointServer() = "${properties.getProtocol()}://${properties.adressServer}:${properties.getPortServer()}"

    override fun getAdressTargetServer() =  properties.adressServer

    override fun setadressTargetServer(adresseIp: String) {
        properties.adressServer=adresseIp
        saveToStorage(KEY_IP_ADDRESS, adresseIp)
    }

    override fun setUserName(nomUser: String) {
        properties.userName=nomUser
        saveToStorage(KEY_USER_NAME, nomUser)

    }

    override fun getUserName(): String = properties.userName

    override fun setMode(isUserMode: Boolean?) {
        properties.isUserMode = isUserMode
        saveToStorage(KEY_MODE, isUserMode.toString())
    }

    override fun getMode(): Boolean? = properties.isUserMode

    override fun setHttpsMode(isHttpsOn: Boolean) {
        properties.isHttpsOn = isHttpsOn
        saveToStorage(KEY_HTTPS_MODE, isHttpsOn.toString())

    }

    override fun getIsHttpsOn() = properties.isHttpsOn

    override fun setUserAuthentication(userAuthentication: UserAuthentication) {
        properties.userAuthentication = userAuthentication
        saveToStorage(KEY_USER_AUTH, Json.encodeToString(userAuthentication))

    }

    override fun getUserAuthentication(): UserAuthentication? = properties.userAuthentication

    private fun saveToStorage(key:String, item:String){
        localStorage.setItem(key, item)
    }
}