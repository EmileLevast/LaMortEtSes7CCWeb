package org.levast.project.configuration

interface IConfiguration {
    fun getEndpointServer(): String
    fun setadressTargetServer(adresseIp:String):Unit
    fun getAdressTargetServer():String
    fun setUserName(nomUser:String)
    fun getUserName():String
    fun setMode(isUserMode: Boolean?)
    fun getMode():Boolean?
    fun setProtocol(isHttpsOn: Boolean)
    fun getProtocol():Boolean?
}