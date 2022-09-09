package com.example.finalproject.entities

interface Health {
    var health:Double

    private fun setHealth(newHealth:Double){health=newHealth}

    fun takeDamage(damage:Damage)
}