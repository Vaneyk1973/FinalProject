package com.example.finalproject.service.interfaces

import com.example.finalproject.service.classes.Damage
import com.google.firebase.database.DatabaseReference

interface Dmg {
    var damage: Damage
    var def:Boolean

    fun doDamage(target: Health)

    fun doDamage(target: Health, ref: DatabaseReference)

    fun defend()
}