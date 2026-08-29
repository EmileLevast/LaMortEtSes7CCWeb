package org.levast.project.affichageMobile

import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.levast.project.configuration.injectNotification

@Composable
fun LayoutNotification(content: @Composable () -> Unit){
    val scope = rememberCoroutineScope()
    val notificationRepository = injectNotification()
    val notification by notificationRepository.uiStateAllNotifications.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    if(notification != null){
        scope.launch {
            snackbarHostState.showSnackbar(notification!!)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        content()
    }
}