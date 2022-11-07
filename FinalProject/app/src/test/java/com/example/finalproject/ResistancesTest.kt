package com.example.finalproject

import com.example.finalproject.service.classes.Resistances
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ResistancesTest {

    private val eps = 1e-9
    private var resistances = Resistances()

    @BeforeEach
    fun setUp() {
        resistances = Resistances()
    }

    @Test
    fun `upgrade resistances test`() {
        resistances.upgradeResistances(arrayListOf(Pair(0, 0.1), Pair(10, 0.3), Pair(-4, 1.0)))
        assertEquals(arrayListOf(0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), resistances.resistances)
        resistances.upgradeResistances(arrayListOf(Pair(0, -0.05), Pair(3, 111.0), Pair(6, -111.0)))
        assertEquals(arrayListOf(0.05, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0), resistances.resistances)
        resistances.upgradeResistances(arrayListOf())
        assertEquals(arrayListOf(0.05, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0), resistances.resistances)
        resistances.upgradeResistances(arrayListOf(Pair(1, 13e-9)))
        assertEquals(
            arrayListOf(0.05, 13e-9, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0),
            resistances.resistances
        )
    }

    @Test
    fun `defence test`() {
        resistances.upgradeResistances(arrayListOf(Pair(0, 0.43), Pair(3, 0.13), Pair(1, 0.9)))
        resistances.applyDefence(2.0)
        assertEquals(arrayListOf(0.86, 1.0, 0.0, 0.26, 0.0, 0.0, 0.0, 0.0), resistances.resistances)
        resistances.removeDefence()
        assertEquals(arrayListOf(0.43, 0.9, 0.0, 0.13, 0.0, 0.0, 0.0, 0.0), resistances.resistances)
    }
}