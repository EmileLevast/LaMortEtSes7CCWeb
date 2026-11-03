package org.levast.project.configuration

import org.levast.project.network.ApiApp
import org.levast.project.network.IImageDownloader
import org.levast.project.repository.IPdfGeneratorRepository

private val configurationImplDesktop = ConfigurationImplDesktop()

actual fun injectConfiguration(): IConfiguration = configurationImplDesktop
actual fun injectPdfGeneratorRepository(): IPdfGeneratorRepository {
    TODO("Not yet implemented")
}