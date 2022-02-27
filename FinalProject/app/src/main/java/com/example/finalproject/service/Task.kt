package com.example.finalproject.service

data class Task(val name:String, val description: String) {
    var completed:Boolean = false
    var taken:Boolean = false
}