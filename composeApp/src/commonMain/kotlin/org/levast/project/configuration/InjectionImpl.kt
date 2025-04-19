package org.levast.project.configuration

import androidx.compose.runtime.Composable
import org.levast.project.network.ApiApp
import org.levast.project.network.IImageDownloader

private val apiApp = ApiApp(getConfiguration())
private val graphicsConstants = GraphicConstantsFullGrid()

fun getApiApp() = apiApp

expect fun getConfiguration(): IConfiguration

fun getGraphicConstants(): GraphicConstantsFullGrid = graphicsConstants