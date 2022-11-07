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

    /**
     * @param defCoefficient the coefficient on which the resistances are multiplied
     * changes the resistances on the event of defence
     */
    fun applyDefence(defCoefficient: Double) {
        if (defCoefficient > 0) {
            beforeDefence.addAll(resistances)
            for (i in resistances.indices)
                resistances[i] =
                    max(0.0, min(1.0, resistances[i] * defCoefficient))
        }
    }

    /**
     * returns the resistances to pre-defence values
     */
    fun removeDefence() {
        resistances.clear()
        resistances.addAll(beforeDefence)
        beforeDefence.clear()
    }

    /**
     * @param upgradedResistances <id of a resistance, the value for it to be increased on>
     * increases the resistances according to the parameter
     */
    fun upgradeResistances(upgradedResistances: ArrayList<Pair<Int, Double>>) {
        for (newResistance in upgradedResistances)
            if (newResistance.first in resistances.indices)
                resistances[newResistance.first] =
                    max(0.0, min(1.0, resistances[newResistance.first] + newResistance.second))
    }
}
