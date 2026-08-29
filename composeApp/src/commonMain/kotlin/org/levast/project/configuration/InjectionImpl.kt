package org.levast.project.configuration

import org.levast.project.network.ApiApp
import org.levast.project.repository.Notification

private val apiApp = ApiApp(injectConfiguration())
private val graphicsConstants = GraphicConstantsFullGrid()
private val notification = Notification()

fun injectApiApp() = apiApp

expect fun injectConfiguration(): IConfiguration

fun injectGraphicConstants(): GraphicConstantsFullGrid = graphicsConstants

fun injectNotification(): Notification = notification