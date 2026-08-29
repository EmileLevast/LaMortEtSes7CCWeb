package org.levast.project.repository

import Joueur
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationRepository() {
    private val _uiStateAllNotifications: MutableStateFlow<String?> = MutableStateFlow(null)
    val uiStateAllNotifications: StateFlow<String?> = _uiStateAllNotifications.asStateFlow()

    suspend fun sendNotification(notification : String){
        _uiStateAllNotifications.emit(notification)
    }
}