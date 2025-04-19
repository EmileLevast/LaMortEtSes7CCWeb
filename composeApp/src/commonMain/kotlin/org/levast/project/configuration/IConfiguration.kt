package org.levast.project.configuration

interface IConfiguration {
    fun getEndpointServer(): String
    fun setIpAdressTargetServer(adresseIp:String):Unit
    fun getIpAdressTargetServer():String
    fun setUserName(nomUser:String)
    fun getUserName():String
    fun setMode(isUserMode: Boolean)
    fun getMode():Boolean?
}