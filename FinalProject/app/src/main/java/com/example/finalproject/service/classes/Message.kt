package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var messageText:String="",
    var userLogin:String="",
    var date:Long=0)