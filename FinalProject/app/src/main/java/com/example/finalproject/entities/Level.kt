package com.example.finalproject.entities

interface Level {
    var level:Int
    var experience:Int
    var experienceToTheNextLevelRequired:Int

    fun levelUp()
}