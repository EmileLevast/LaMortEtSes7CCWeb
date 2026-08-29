package org.levast.project.configuration

private val configuration = ConfigurationImpl()

actual fun injectConfiguration(): IConfiguration = configuration
