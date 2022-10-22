package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable

@Serializable
data class Task(val description: String, val name:String) {
    var completed:Boolean = false
    var taken:Boolean = false
}