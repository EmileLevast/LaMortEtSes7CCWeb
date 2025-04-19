package org.levast.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "LaMortEtSes7CCWeb",
    ) {
        App()
    }
}