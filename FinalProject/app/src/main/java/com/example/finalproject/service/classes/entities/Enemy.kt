package com.example.finalproject.service.classes.entities

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Loot
import com.example.finalproject.service.classes.Resistances
import com.example.finalproject.service.interfaces.Dmg
import com.example.finalproject.service.interfaces.Health
import com.example.finalproject.service.serializers.EnemySerializer
import com.google.firebase.database.DatabaseReference
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.HashMap

@Serializable(with = EnemySerializer::class)
class Enemy(
    name: String,
    id: Int,
    health: Double,
    maxHealth: Double,
    healthRegen: Double,
    mana: Double,
    maxMana: Double,
    manaRegen: Double,
    resistances: Resistances,
    loot: Loot,
    override var damage: Damage
) :
    Entity(
        name = name,
        id = id,
        health = health,
        maxHealth = maxHealth,
        healthRegen = healthRegen,
        mana = mana,
        maxMana = maxMana,
        manaRegen = manaRegen,
        resistances = resistances,
        loot = loot
    ), Dmg {
    override var def: Boolean = false

    constructor(enemy: Enemy) : this(
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

    constructor(entity: Entity, damage: Damage) : this(
        entity.name,
        entity.id,
        entity.health,
        entity.maxHealth,
        entity.healthRegen,
        entity.mana,
        entity.maxMana,
        entity.manaRegen,
        entity.resistances,
        entity.loot,
        damage
    )

    private var tick = 1
    private val defCoefficient = 1.05

    override fun doDamage(target: Health) {
        target.takeDamage(damage)
    }

    override fun doDamage(target: Health, ref: DatabaseReference) {
        target.takeDamage(damage, ref)
    }

    fun attack(target: Health) {
        if (tick % 2 == 0) {
            defend()
            doDamage(target)
        } else {
            defend()
        }
        tick++
    }

    override fun defend() {
        def = !def
        if (def)
            resistances.applyDefence(defCoefficient)
        else
            resistances.removeDefence(defCoefficient)
    }

    override fun takeDamage(damage: Damage, ref: DatabaseReference) {
        super.takeDamage(damage, ref)
        ref.setValue(Json.encodeToString(serializer(), this))
    }
}