package org.levast.project.configuration

import kotlinx.serialization.Serializable
import org.levast.project.SERVER_KTOR_PORT
import org.levast.project.SERVER_KTOR_PORT_SSL

@Serializable
class AppProperties(var adressServer:String="", var userName:String = "", var isUserMode:Boolean?= null, var isHttpsOn : Boolean = true, var userAuthentication: UserAuthentication? = null){

    fun getPortServer() = if(isHttpsOn) SERVER_KTOR_PORT_SSL else SERVER_KTOR_PORT

    fun getProtocol() = if(isHttpsOn) "https" else "http"
}

@Serializable
data class UserAuthentication(val userName:String, val password:String)