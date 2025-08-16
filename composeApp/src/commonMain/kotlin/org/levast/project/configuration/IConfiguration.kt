package org.levast.project.configuration

interface IConfiguration {
    fun getEndpointServer(): String
    fun setadressTargetServer(adresseIp:String):Unit
    fun getAdressTargetServer():String
    fun setUserName(nomUser:String)
    fun getUserName():String
    fun setMode(isUserMode: Boolean?)
    fun getMode():Boolean?
    fun setHttpsMode(isHttpsOn: Boolean)
    fun getIsHttpsOn(): Boolean
    fun setUserAuthentication(userAuthentication: UserAuthentication)
    fun getUserAuthentication(): UserAuthentication?
}