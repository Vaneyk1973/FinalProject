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
    private val components: ArrayList<Component> = ArrayList()

    constructor(
        element: Element,
        type: Type,
        form: Form,
        manaChannel: ManaChannel,
        manaReservoir: ManaReservoir,
        name: String
    ) : this() {
        components.add(Element(element))
        components.add(Type(type))
        components.add(Form(form))
        components.add(ManaChannel(manaChannel))
        components.add(ManaReservoir(manaReservoir))
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
        components.add(assets.components[assets.elements[element - 1]] as Element)
        components.add(assets.components[assets.types[type]] as Type)
    }

    constructor(spell: Spell) : this() {
        components.clear()
        components.addAll(spell.components)
        damage = spell.damage
        lastingTime = spell.lastingTime
        manaConsumption = spell.manaConsumption
        name = spell.name
    }

    private fun consumeMana() {
        player.mana -= manaConsumption
    }

    fun affect(target: Health) {
        if (player.mana >= manaConsumption) {
            consumeMana()
            target.takeDamage(damage)
        }
    }

    fun getElement(): Element = Element(components[0] as Element)
    fun getType(): Type = Type(components[1] as Type)
}