package com.example.finalproject.entities

import com.example.finalproject.service.spell.Spell

interface Mana {
    var mana:Double
    var maxMana:Double
    var manaRegen:Double

    fun castSpell(target:Health, spell:Spell)

    fun regenerateMana()
}