package com.example.finalproject.service

data class Task(val description: String, val name:String) {
    var completed:Boolean = false
    var taken:Boolean = false
}