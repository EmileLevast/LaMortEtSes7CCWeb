package org.levast.project.configuration

import org.levast.project.repository.IPdfGeneratorRepository


actual fun injectConfiguration(): IConfiguration = configurationImpl
actual fun injectPdfGeneratorRepository(): IPdfGeneratorRepository {
    TODO("Not yet implemented")
}