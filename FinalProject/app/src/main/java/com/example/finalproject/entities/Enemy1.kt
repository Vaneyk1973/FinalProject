package com.example.finalproject.entities

import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference

class Enemy1(
    name: String,
    id: Int,
    health: Double,
    resistances: ArrayList<Double>,
    loot: Loot,
    override var damage: Damage
) :
    Entity1(name = name, id = id, health = health, resistances = resistances, loot=loot), Dmg {

    var tick=0
    var def=false

    override fun doDamage(target: Health) {
        target.takeDamage(damage)
    }

    override fun doDamage(target: Health, ref: DatabaseReference) {
        target.takeDamage(damage, ref)
    }

    override fun startFight(){tick=0}

    override fun attack(target: Health){
        if (tick%2==0){
            defend()
            doDamage(target)
        } else {
            defend()
        }
        tick++
    }

    override fun defend(){
        if (def)
            resistances[0]/=1.1
        else
            resistances[0]*=1.1
    }
}