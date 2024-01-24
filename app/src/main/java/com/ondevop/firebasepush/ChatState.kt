package com.ondevop.firebasepush

data class ChatState(
    val isEnteringToken: Boolean = false,
    val remoteToken: String = "",
    val messageText: String = ""
)
