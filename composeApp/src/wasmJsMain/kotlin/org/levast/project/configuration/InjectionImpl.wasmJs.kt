package org.levast.project.configuration

import androidx.compose.runtime.Composable
import org.levast.project.network.ApiApp
import org.levast.project.network.IImageDownloader

@Composable
actual fun getConfiguration(): IConfiguration = configurationImpl

@Composable
actual fun getApiApp(): ApiApp = apiApp

@Composable
actual fun getImageDownloader(): IImageDownloader = imageDownloaderImpl

@Composable
actual fun getGraphicConstants(): GraphicConstantsFullGrid = graphicConstantsFullGrid