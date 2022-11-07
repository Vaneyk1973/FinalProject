package com.example.finalproject.service.classes.spell

import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.interfaces.Health
import kotlinx.serialization.Serializable

@Serializable
class Spell() {
    var manaConsumption: Double = 0.0
    lateinit var damage: Damage
    private var lastingTime: Double = 0.0
    var name: String = ""
    private lateinit var element: Element
    private lateinit var type: Type
    private lateinit var form: Form
    private lateinit var manaChannel: ManaChannel
    private lateinit var manaReservoir: ManaReservoir

    constructor(
        element: Element,
        type: Type,
        form: Form,
        manaChannel: ManaChannel,
        manaReservoir: ManaReservoir,
        name: String
    ) : this() {
        this.element = element
        this.form = form
        this.type = type
        this.manaChannel = manaChannel
        this.manaReservoir = manaReservoir
        this.name = name
        manaConsumption = manaReservoir.volume * 3
        val a = ArrayList<Double>()
        for (i in 0 until 10)
            a.add(0.0)
        a[element.element] = manaReservoir.volume * element.baseDamage / 2
        damage = Damage(a)
        lastingTime = manaReservoir.volume / manaChannel.bandwidth
    }

    constructor(
        name: String,
        manaConsumption: Double,
        damage: Damage,
        lastingTime: Double = 0.0,
        element: Int = 1,
        type: Int = 0
    ) : this() {
        this.name = name
        this.manaConsumption = manaConsumption
        this.damage = damage
        this.lastingTime = lastingTime
        this.element = assets.elements[element + 1023]!!
        this.type = assets.types[type + 1034]!!
    }

    constructor(spell: Spell) : this() {
        element = spell.element
        form = spell.form
        type = spell.type
        manaReservoir = spell.manaReservoir
        manaChannel = spell.manaChannel
        damage = spell.damage
        lastingTime = spell.lastingTime
        manaConsumption = spell.manaConsumption
        name = spell.name
    }

    /**
     * consumes mana from the player
     */
    private fun consumeMana() {
        player.mana -= manaConsumption
    }

    /**
     * @param target the target of a spell to affect
     * damages the target
     */
    fun affect(target: Health) {
        if (player.mana >= manaConsumption) {
            consumeMana()
            target.takeDamage(damage)
        }
    }

    /**
     * @return the element component
     */
    fun getElement(): Element = Element(element)

    /**
     * @return the type component
     */
    fun getType(): Type = Type(type)
}