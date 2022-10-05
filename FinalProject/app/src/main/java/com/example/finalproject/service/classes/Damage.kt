package com.example.finalproject.service.classes

class Damage(private val dmg:ArrayList<Double>) {

    init {
        while (dmg.size<10)
            dmg.add(0.0)
    }

    constructor():this(ArrayList())

    constructor(damage: Damage):this(){
        dmg.clear()
        dmg.addAll(damage.dmg)
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyResistances(resistances:ArrayList<Double>): ArrayList<Double> {
        val newDmg=dmg.clone() as ArrayList<Double>
        for (i in 0 until newDmg.size)
            newDmg[i]*=resistances[i]
        return newDmg
    }

    fun realDamage(resistances:ArrayList<Double>): Double {
        return applyResistances(resistances=resistances).sum()
    }
}