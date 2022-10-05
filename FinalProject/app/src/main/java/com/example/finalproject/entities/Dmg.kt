package com.example.finalproject.entities

import com.google.firebase.database.DatabaseReference

interface Dmg {
    var damage: Damage

    fun doDamage(target: Health)

    fun doDamage(target: Health, ref: DatabaseReference)

    fun defend()
}