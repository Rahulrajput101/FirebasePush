package com.ondevop.firebasepush

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException

class ChatViewModel : ViewModel(){

     var state by mutableStateOf(ChatState())
         private set



    private val aPi: FcmAPi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    init {
        viewModelScope.launch {
          Firebase.messaging.subscribeToTopic("chat").await()

        }
    }
    fun  onRemoteTokenChange(newToken: String){
        state =state.copy(
            remoteToken = newToken
        )
    }


    fun onSubmitToken() {
        state = state.copy(
            isEnteringToken = false
        )
    }
    fun onMessageChange(message :String) {
        state = state.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadCast: Boolean){
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if(isBroadCast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "New Message!",
                    body = state.messageText
                )
            )

             try {
                 if(isBroadCast){
                     aPi.broadcast(messageDto)
                 }else{
                     aPi.sendMessage(messageDto)
                 }

                 state = state.copy(messageText = "")
             }catch (e: HttpException){
                 e.printStackTrace()
             } catch (e: IOException){
                 e.printStackTrace()
             }
        }
    }
}