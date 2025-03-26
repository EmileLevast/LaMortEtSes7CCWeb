package org.levast.project.configuration

import configuration.GraphicConstantsFullGrid
import network.ApiApp

val graphicConstantsFullGrid = GraphicConstantsFullGrid()
val configurationImpl = ConfigurationImpl()
val imageDownloaderImpl = ImageDownloaderImpl(configurationImpl)
val apiApp = ApiApp(configurationImpl, imageDownloaderImpl)
