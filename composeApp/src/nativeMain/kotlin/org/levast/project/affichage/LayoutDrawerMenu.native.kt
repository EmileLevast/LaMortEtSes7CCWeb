package org.levast.project.affichage

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable

@Composable
actual fun LayoutDrawerMenu(
    content: @Composable (innerPadding: PaddingValues) -> Unit,
    contentOption: @Composable() () -> Unit,
    drawerState: DrawerState
) {
}