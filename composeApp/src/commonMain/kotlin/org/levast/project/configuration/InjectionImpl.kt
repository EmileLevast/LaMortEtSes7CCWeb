package org.levast.project.configuration

import androidx.compose.runtime.Composable
import org.levast.project.network.ApiApp
import org.levast.project.network.IImageDownloader

val apiApp = ApiApp(getConfiguration())
val graphicsConstants = GraphicConstantsFullGrid()

fun getApiApp() = apiApp

expect fun getConfiguration(): IConfiguration

fun getGraphicConstants(): GraphicConstantsFullGrid = graphicsConstants