package org.levast.project.configuration

private val configuration = ConfigurationImpl()

actual fun getConfiguration(): IConfiguration = configuration
