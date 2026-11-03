package org.levast.project.configuration

import org.levast.project.repository.IPdfGeneratorRepository

private val configuration = ConfigurationImpl()

actual fun injectConfiguration(): IConfiguration = configuration
actual fun injectPdfGeneratorRepository(): IPdfGeneratorRepository {
    TODO("Not yet implemented")
}