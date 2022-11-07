package com.example.finalproject

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Resistances
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class DamageTest {

    private val eps = 1e-8

    @Test
    fun `damage constructors test`() {
        val damage1 = Damage()
        val damage2 = Damage(arrayListOf(1.0, 5.0, -1.31, 21.031, 13.9, 113.0, 0.000000001, 99.13))
        val damage3 = Damage(arrayListOf(93.0, 1.3))
        val damage4 = Damage(
            arrayListOf(
                1.0,
                5.0,
                -1.31,
                21.031,
                13.9,
                113.0,
                0.000000001,
                99.13,
                93.0,
                1.3,
                93.0,
                1.3
            )
        )
        assertEquals(damage1.dmg, arrayListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
        assertEquals(
            damage2.dmg,
            arrayListOf(1.0, 5.0, -1.31, 21.031, 13.9, 113.0, 0.000000001, 99.13)
        )
        assertEquals(damage3.dmg, arrayListOf(93.0, 1.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
        assertEquals(
            damage4.dmg,
            arrayListOf(1.0, 5.0, -1.31, 21.031, 13.9, 113.0, 0.000000001, 99.13)
        )
        assertEquals(damage2.dmg, damage4.dmg)
    }

    @Test
    fun `real damage calculation test`() {
        val damage1 = Damage()
        val damage2 = Damage(arrayListOf(1.0, 5.0, -1.31, 21.031, 13.9, 113.0, 0.0, 99.13))
        val damage3 = Damage(arrayListOf(93.0, 1.3))
        val resistances1 = Resistances()
        assertTrue((damage1.realDamage(resistances1) - 0.0).absoluteValue <= eps)
        assertTrue((damage2.realDamage(resistances1) - 251.751).absoluteValue <= eps)
        assertTrue((damage3.realDamage(resistances1) - 94.3).absoluteValue <= eps)
        resistances1.upgradeResistances(arrayListOf(Pair(0, 0.5), Pair(4, 1.0)))
        assertTrue((damage1.realDamage(resistances1) - 0.0).absoluteValue <= eps)
        assertTrue((damage2.realDamage(resistances1) - 237.351).absoluteValue <= eps)
        assertTrue((damage3.realDamage(resistances1) - 47.8).absoluteValue <= eps)
        resistances1.upgradeResistances(arrayListOf(Pair(-1, 0.5), Pair(8, 1.6)))
    }
}