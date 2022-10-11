package com.example.finalproject.service.interfaces

import com.example.finalproject.service.classes.spell.Spell

interface Mana {
    var mana: Double
    var maxMana: Double
    var manaRegen: Double

    fun castSpell(target: Health, spell: Spell)

    fun regenerateMana()
}