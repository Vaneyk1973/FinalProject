package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable

@Serializable
class Resistances() {
    val resistances: ArrayList<Double> = ArrayList()

    init {
        while (resistances.size < 8)
            resistances.add(0.0)
    }

    constructor(resistances: ArrayList<Double>) : this() {
        for (i in resistances.indices)
            this.resistances[i] = resistances[i]
    }

    fun applyDefence(defCoefficient: Double) {
        for (i in resistances.indices)
            resistances[i] *= defCoefficient
    }

    fun removeDefence(defCoefficient: Double) {
        for (i in resistances.indices)
            resistances[i] /= defCoefficient
    }

    fun upgradeResistances(upgradedResistances: ArrayList<Pair<Int, Double>>) {
        for (newResistance in upgradedResistances)
            resistances[newResistance.first] += newResistance.second
    }
}
