package com.example.finalproject.service.classes

data class Task(val description: String, val name:String) {
    var completed:Boolean = false
    var taken:Boolean = false
}