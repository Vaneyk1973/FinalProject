package com.example.finalproject.entities

@Suppress("UNCHECKED_CAST")

class Damage(private val dmg:ArrayList<Double>) {

    private fun applyResistances(resistances:ArrayList<Double>): ArrayList<Double> {
        val newDmg=dmg.clone() as ArrayList<Double>
        for (i in 0..newDmg.size)
            newDmg[i]*=resistances[i]
        return newDmg
    }

    fun realDamage(resistances:ArrayList<Double>): Double {
        return applyResistances(resistances=resistances).sum()
    }
}