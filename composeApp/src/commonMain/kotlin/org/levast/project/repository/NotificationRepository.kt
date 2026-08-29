package org.levast.project.repository

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationRepository() {
    private val _notifications: MutableSharedFlow<String?> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 32,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiStateAllNotifications: SharedFlow<String?> = _notifications.asSharedFlow()

    fun sendNotification(notification : String){
        _notifications.tryEmit(notification)
    }
}