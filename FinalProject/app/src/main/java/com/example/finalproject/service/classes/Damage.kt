package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable
import java.lang.Double.min
import kotlin.math.min

@Serializable
class Damage() {
    val dmg = ArrayList<Double>()

    init {
        while (dmg.size < 8)
            dmg.add(0.0)
    }

    constructor(dmg: ArrayList<Double>) : this() {
        for (i in 0 until min(this.dmg.size, dmg.size))
            this.dmg[i] = dmg[i]
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyResistances(resistances: Resistances): ArrayList<Double> {
        val newDmg = dmg.clone() as ArrayList<Double>
        for (i in 0 until newDmg.size)
            newDmg[i] *= (1 - resistances.resistances[i])
        return newDmg
    }

    fun realDamage(resistances: Resistances): Double {
        return applyResistances(resistances = resistances).sum()
    }

    fun upgradeDamage(addedDamage: ArrayList<Pair<Int, Double>>) {
        for (newDamage in addedDamage) {
            dmg[newDamage.first] *= 1 + newDamage.second
        }
    }

    override fun toString(): String = dmg.sum().toString()
}