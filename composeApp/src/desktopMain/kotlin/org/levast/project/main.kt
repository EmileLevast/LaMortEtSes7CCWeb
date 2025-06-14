package org.levast.project

import App
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.iconchapeau
import org.jetbrains.compose.resources.painterResource

fun main() = application {

    val icon = painterResource(Res.drawable.iconchapeau)

    Tray(
        icon = icon,
        menu = {
            Item("Quit App", onClick = ::exitApplication)
        }
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "LaMortEtSes7CCWeb",
        icon = icon
    ) {
        App()
    }
}