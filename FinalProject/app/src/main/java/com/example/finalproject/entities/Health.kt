package com.example.finalproject.entities

import com.google.firebase.database.DatabaseReference

interface Health {
    var health:Double
    var healthRegen:Double
    var resistances:ArrayList<Double>

    fun takeDamage(damage:Damage)

    fun takeDamage(damage:Damage, ref:DatabaseReference)
}