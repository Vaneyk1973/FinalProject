package com.example.finalproject.service.interfaces

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Resistances
import com.google.firebase.database.DatabaseReference

interface Health {
    var health: Double
    var healthRegen: Double
    var maxHealth: Double
    var resistances: Resistances

    fun takeDamage(damage: Damage)

    fun takeDamage(damage: Damage, ref: DatabaseReference)

    fun regenerateHealth()
}