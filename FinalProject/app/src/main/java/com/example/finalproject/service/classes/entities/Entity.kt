package com.example.finalproject.service.classes.entities

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Loot
import com.example.finalproject.service.classes.Resistances
import com.example.finalproject.service.classes.Unit
import com.example.finalproject.service.classes.spell.Spell
import com.example.finalproject.service.interfaces.Health
import com.example.finalproject.service.interfaces.Lootable
import com.example.finalproject.service.interfaces.Mana
import com.example.finalproject.service.serializers.EntitySerializer
import com.google.firebase.database.DatabaseReference
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

@Serializable(with = EntitySerializer::class)
open class Entity(
    name: String = "",
    id: Int = 0,
    override var health: Double = 0.0,
    override var maxHealth: Double = 0.0,
    override var healthRegen: Double = 0.0,
    override var mana: Double = 0.0,
    override var maxMana: Double = 0.0,
    override var manaRegen: Double = 0.0,
    override var resistances: Resistances = Resistances(),
    override val loot: Loot = Loot()
) :
    Unit(name = name, id = id), Health, Mana, Lootable {

    constructor(entity: Entity) : this(
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
    )

    /**
     * @param damage the damage taken
     * calculates the real damage and decreases the health
     */
    override fun takeDamage(damage: Damage) {
        health = max(0.0, health - damage.realDamage(resistances))
    }

    /**
     * @param damage the damage taken
     * @param ref the Firebase reference to an object
     */
    override fun takeDamage(damage: Damage, ref: DatabaseReference) {
        takeDamage(damage)
    }

    /**
     * regenerates health
     */
    override fun regenerateHealth() {
        health = min(maxHealth, health + healthRegen)
    }

    /**
     * @param target the target on which the spell will be casted
     * @param spell the spell to be casted
     * affects the target with the spell
     */
    override fun castSpell(target: Health, spell: Spell) {
        spell.affect(target)
    }

    /**
     * regenerates mana
     */
    override fun regenerateMana() {
        mana = min(maxMana, mana + manaRegen)
    }

    /**
     * regenerates health and mana
     */
    fun regenerate() {
        regenerateHealth()
        regenerateMana()
    }

    /**
     * @param playerReference the Firebase reference to an object in the database
     */
    open fun regenerate(playerReference: DatabaseReference) {
        regenerate()
    }
}