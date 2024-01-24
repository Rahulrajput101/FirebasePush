package com.ondevop.firebasepush

import android.app.Notification

data class SendMessageDto(
     val to: String,
     val notification: NotificationBody
 )

data class NotificationBody (
     val title : String,
     val body: String
)
