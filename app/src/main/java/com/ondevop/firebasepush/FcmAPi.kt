package com.ondevop.firebasepush

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmAPi {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcasr")
    suspend fun broadcast(
        @Body body: SendMessageDto
    )
}