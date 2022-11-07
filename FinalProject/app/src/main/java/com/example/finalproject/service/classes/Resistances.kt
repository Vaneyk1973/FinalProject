package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable
import java.lang.Double.max
import java.lang.Double.min

@Serializable
class Resistances {
    val resistances: ArrayList<Double> = ArrayList()
    private val beforeDefence: ArrayList<Double> = ArrayList()

    init {
        while (resistances.size < 8)
            resistances.add(0.0)
    }

    fun applyDefence(defCoefficient: Double) {
        if (defCoefficient > 0) {
            beforeDefence.addAll(resistances)
            for (i in resistances.indices)
                resistances[i] =
                    max(0.0, min(1.0, resistances[i] * defCoefficient))
        }
    }

    fun removeDefence(defCoefficient: Double) {
        if (defCoefficient > 0) {
            resistances.clear()
            resistances.addAll(beforeDefence)
            beforeDefence.clear()
        }
    }

    fun upgradeResistances(upgradedResistances: ArrayList<Pair<Int, Double>>) {
        for (newResistance in upgradedResistances)
            if (newResistance.first in resistances.indices)
                resistances[newResistance.first] =
                    max(0.0, min(1.0, resistances[newResistance.first] + newResistance.second))
    }
}
