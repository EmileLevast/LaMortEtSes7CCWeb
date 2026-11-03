package org.levast.project.configuration

import org.levast.project.network.ApiApp
import org.levast.project.repository.NotificationRepository
import org.levast.project.repository.IPdfGeneratorRepository

private val notification = NotificationRepository()

private val apiApp = ApiApp(injectConfiguration(), notification)
private val graphicsConstants = GraphicConstantsFullGrid()

fun injectApiApp() = apiApp

expect fun injectConfiguration(): IConfiguration

expect fun injectPdfGeneratorRepository(): IPdfGeneratorRepository

fun injectGraphicConstants(): GraphicConstantsFullGrid = graphicsConstants

fun injectNotification(): NotificationRepository = notification