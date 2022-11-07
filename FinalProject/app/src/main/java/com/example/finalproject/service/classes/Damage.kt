package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable
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

    /**
     * @param resistances the elemental resistances of a target
     * @return the damage after applying resistances
     * applies the resistances to the damage
     */
    @Suppress("UNCHECKED_CAST")
    private fun applyResistances(resistances: Resistances): ArrayList<Double> {
        val newDmg = dmg.clone() as ArrayList<Double>
        for (i in 0 until newDmg.size)
            newDmg[i] *= (1 - resistances.resistances[i])
        return newDmg
    }

    /**
     * @param resistances the elemental resistances of a target
     * @return the sum of elemental damages
     * applies the resistances and calculates the sum of all damages
     */
    fun realDamage(resistances: Resistances): Double {
        return applyResistances(resistances = resistances).sum()
    }

    /**
     * @param addedDamage <id of an element, upgrade of a damage in %/100>
     * upgrades the damage according to addedDamage
     */
    fun upgradeDamage(addedDamage: ArrayList<Pair<Int, Double>>) {
        for (newDamage in addedDamage) {
            dmg[newDamage.first] *= 1 + newDamage.second
        }
    }

    /**
     * @return the string value of an object
     */
    override fun toString(): String = dmg.sum().toString()
}