package com.example.finalproject.service.interfaces

interface Level {
    var level:Int
    var experience:Int
    var experienceToTheNextLevelRequired:Int

    fun levelUp()
}