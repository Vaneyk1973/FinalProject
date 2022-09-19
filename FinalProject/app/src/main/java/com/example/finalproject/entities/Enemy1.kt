package com.example.finalproject.entities

class Enemy1(
    name: String,
    id: Int,
    health: Double,
    resistances: ArrayList<Double>,
    val dmg: Damage
) :
    Entity1(name = name, id = id, health = health, resistances = resistances), Dmg {

    override fun doDamage(dmg: Damage, target: Health) {
        target.takeDamage(dmg)
    }
}