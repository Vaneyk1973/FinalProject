package com.example.finalproject.entities

import com.example.finalproject.service.spell.Spell
import com.google.firebase.database.DatabaseReference
import kotlin.math.max

class Enemy(
    name: String,
    id: Int,
    health: Double,
    maxHealth: Double,
    healthRegen: Double,
    mana:Double,
    maxMana:Double,
    manaRegen:Double,
    resistances: ArrayList<Double>,
    loot: Loot,
    override var damage: Damage
) :
    Entity(
        name = name,
        id = id,
        health = health,
        maxHealth = maxHealth,
        healthRegen = healthRegen,
        mana=mana,
        maxMana=maxMana,
        manaRegen=manaRegen,
        resistances = resistances,
        loot = loot
    ), Dmg {

    constructor(enemy:Enemy):this(
        enemy.name,
        enemy.id,
        enemy.health,
        enemy.maxHealth,
        enemy.healthRegen,
        enemy.mana,
        enemy.maxMana,
        enemy.manaRegen,
        enemy.resistances,
        enemy.loot,
        enemy.damage
    )

    var tick = 0
    var def = false

    override fun doDamage(target: Health) {
        target.takeDamage(damage)
    }

    override fun doDamage(target: Health, ref: DatabaseReference) {
        target.takeDamage(damage, ref)
    }

    override fun startFight() {
        tick = 0
    }

    override fun attack(target: Health) {
        if (tick % 2 == 0) {
            defend()
            doDamage(target)
        } else {
            defend()
        }
        tick++
    }

    override fun defend() {
        if (def)
            resistances[0] /= 1.1
        else
            resistances[0] *= 1.1
    }
}