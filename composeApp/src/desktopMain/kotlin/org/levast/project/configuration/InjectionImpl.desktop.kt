package org.levast.project.configuration

import org.levast.project.network.ApiApp
import org.levast.project.network.IImageDownloader

private val configurationImplDesktop = ConfigurationImplDesktop()

actual fun getConfiguration(): IConfiguration = configurationImplDesktop