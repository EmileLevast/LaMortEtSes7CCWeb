package org.levast.project.configuration

import org.levast.project.configuration.GraphicConstantsFullGrid
import org.levast.project.network.ApiApp

val graphicConstantsFullGrid = GraphicConstantsFullGrid()
val configurationImpl = ConfigurationImpl()
val imageDownloaderImpl = ImageDownloaderImpl(configurationImpl)
val apiApp = ApiApp(configurationImpl, imageDownloaderImpl)
