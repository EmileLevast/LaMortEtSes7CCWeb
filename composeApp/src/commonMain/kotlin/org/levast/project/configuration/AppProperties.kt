package org.levast.project.configuration

import kotlinx.serialization.Serializable
import org.levast.project.SERVER_PORT

@Serializable
class AppProperties(var ipAdressServer:String="", var userName:String = "", val portServer: Int = SERVER_PORT)