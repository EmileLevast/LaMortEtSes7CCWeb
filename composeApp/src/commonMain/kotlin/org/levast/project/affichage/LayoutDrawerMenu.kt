package org.levast.project.affichage

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable

@Composable
expect fun LayoutDrawerMenu(
    content: @Composable() () -> Unit,
    contentOption: @Composable() () -> Unit,
    drawerState: DrawerState
)