package org.levast.project.configuration

import androidx.compose.runtime.Composable
import org.levast.project.network.ApiApp
import org.levast.project.network.IImageDownloader

@Composable
expect fun getConfiguration(): IConfiguration

@Composable
expect fun getApiApp(): ApiApp

@Composable
expect fun getImageDownloader(): IImageDownloader

@Composable
expect fun getGraphicConstants(): GraphicConstantsFullGrid