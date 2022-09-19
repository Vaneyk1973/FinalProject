package com.example.finalproject.entities

import kotlin.math.max

class Entity1(name:String, id:Int, var health:Double, var resistances:ArrayList<Double>):Unit(name=name, id=id), Health {

    override fun takeDamage(damage: Damage) {
        health=max(0.0, health-damage.realDamage(resistances))
    }
}