package com.example.finalproject.service.classes.entities

import android.graphics.Bitmap
import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Loot
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
    override var resistances: ArrayList<Double> = ArrayList(),
    override val loot: Loot = Loot()
) :
    Unit(name = name, id = id), Health, Mana, Lootable {

    var texture: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        get() = Bitmap.createBitmap(field)
        set(texture) {
            field = Bitmap.createBitmap(texture)
        }

    constructor(
        name: String,
        id: Int,
        health: Double,
        maxHealth: Double,
        healthRegen: Double,
        mana: Double,
        maxMana: Double,
        manaRegen: Double,
        resistances: ArrayList<Double>,
        loot: Loot,
        texture: Bitmap
    ) : this(
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
    ) {
        this.texture = texture
    }

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
        entity.texture
    )

    override fun takeDamage(damage: Damage) {
        health = max(0.0, health - damage.realDamage(resistances))
    }

    override fun takeDamage(damage: Damage, ref: DatabaseReference) {
        ref.child("health").setValue(max(0.0, health - damage.realDamage(resistances)))
    }

    override fun regenerateHealth() {
        health = min(maxHealth, health + healthRegen)
    }

    override fun castSpell(target: Health, spell: Spell) {
        spell.affect(target)
    }

    override fun regenerateMana() {
        mana = min(maxMana, mana + manaRegen)
    }

    fun regenerate() {
        regenerateHealth()
        regenerateMana()
    }
}