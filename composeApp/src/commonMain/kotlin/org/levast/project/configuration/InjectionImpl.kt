package org.levast.project.configuration

import org.levast.project.network.ApiApp
import org.levast.project.repository.NotificationRepository

private val apiApp = ApiApp(injectConfiguration())
private val graphicsConstants = GraphicConstantsFullGrid()
private val notification = NotificationRepository()

fun injectApiApp() = apiApp

expect fun injectConfiguration(): IConfiguration

fun injectGraphicConstants(): GraphicConstantsFullGrid = graphicsConstants

fun injectNotification(): NotificationRepository = notification