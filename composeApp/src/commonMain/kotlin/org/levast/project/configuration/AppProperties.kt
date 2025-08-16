package org.levast.project.configuration

import kotlinx.serialization.Serializable
import org.levast.project.SERVER_KTOR_PORT
import org.levast.project.SERVER_KTOR_PORT_SSL

/**
 * userName : c'est le nom du joueur que l'utilisateur veut voir dans l'application, basiquement c'est lui-même pour suivre le jeu, mais pas forcement (le MJ peut aimer naviguer vers d'autres joueurs)
 * userAuthentication : contient le nom de l'utilisateur de l'application et son mot de passe associé.
 * C'est en cours de dev mais l'idée est qu'un utilisateur ne puisse faire des commandes put que sur le profil de joueur associé
 */
@Serializable
class AppProperties(var adressServer:String="", var userName:String = "", var isUserMode:Boolean?= null, var isHttpsOn : Boolean = true, var userAuthentication: UserAuthentication? = null){

    fun getPortServer() = if(isHttpsOn) SERVER_KTOR_PORT_SSL else SERVER_KTOR_PORT

    fun getProtocol() = if(isHttpsOn) "https" else "http"
}

@Serializable
data class UserAuthentication(val userName:String, val password:String)