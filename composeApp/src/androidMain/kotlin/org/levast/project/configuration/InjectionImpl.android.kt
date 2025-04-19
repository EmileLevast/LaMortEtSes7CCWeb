package org.levast.project.configuration

val configuration = ConfigurationImpl()

actual fun getConfiguration(): IConfiguration = configuration
