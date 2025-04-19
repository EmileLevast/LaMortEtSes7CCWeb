package org.levast.project.affichageMobile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun HandleBackButton(onClickBack: () -> Unit) {
    BackHandler {
        onClickBack()
    }
}