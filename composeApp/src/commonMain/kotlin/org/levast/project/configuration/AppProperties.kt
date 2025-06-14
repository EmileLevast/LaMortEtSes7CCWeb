package org.levast.project.configuration

import kotlinx.serialization.Serializable
import org.levast.project.SERVER_KTOR_PORT
import org.levast.project.SERVER_KTOR_PORT_SSL

@Serializable
class AppProperties(var ipAdressServer:String="", var userName:String = "",var isUserMode:Boolean?= null, val portServer: Int = SERVER_KTOR_PORT_SSL)