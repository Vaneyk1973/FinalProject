package com.example.finalproject.service.classes

import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.service.classes.spell.Element
import com.example.finalproject.service.classes.spell.Form
import com.example.finalproject.service.classes.spell.ManaChannel
import com.example.finalproject.service.classes.spell.ManaReservoir
import com.example.finalproject.service.classes.spell.Spell
import com.example.finalproject.service.classes.spell.Type
import kotlinx.serialization.Serializable

@Serializable
class ResearchEffect(
    val name: String,
    val id: Int,
    private val affectedResearches: ArrayList<Int> = ArrayList(),
    private val unlockedComponents: ArrayList<Int> = ArrayList(),
    private val upgradedResistances: ArrayList<Pair<Int, Double>> = ArrayList(),
    private val upgradedDamage: ArrayList<Pair<Int, Double>> = ArrayList(),
    private val addedSpells: ArrayList<Spell> = ArrayList()
) {
    fun affect() {
        for (research in affectedResearches) {
            if (assets.researches[research]?.enable() == true && assets.researches[research]?.researched == false)
                assets.availableResearches.add(research)
        }
        for (component in unlockedComponents) {
            assets.components[component]?.avail()
        }
        for (spell in addedSpells)
            player.spells.add(spell)
        player.resistances.upgradeResistances(upgradedResistances)
        player.damage.upgradeDamage(upgradedDamage)
    }

    override fun toString(): String {
        var returnString = ""
        var componentsString = ""
        var resistancesString = ""
        var damageString = ""
        var spellsString = ""
        if (unlockedComponents.isNotEmpty()) {
            componentsString += "unlocks the following components: "
            for (i in unlockedComponents) {
                when (val comp = assets.components[i]) {
                    is Element -> componentsString += "Element ${comp.name}, "
                    is Type -> componentsString += "Type ${comp.name}, "
                    is Form -> componentsString += "Form ${comp.name}, "
                    is ManaChannel -> componentsString += "Mana Channel ${comp.name}, "
                    is ManaReservoir -> componentsString += "Mana Reservoir ${comp.name}, "
                }
            }
            returnString += componentsString.dropLast(2) + "\n"
        }
        if (upgradedDamage.isNotEmpty()) {
            damageString += "increases your damage: "
            for (i in upgradedDamage) {
                when (i.first) {
                    0 -> damageString += "Physical"
                    1 -> damageString += "Pure mana"
                    2 -> damageString += "Fire"
                    3 -> damageString += "Water"
                    4 -> damageString += "Air"
                    5 -> damageString += "Earth"
                    6 -> damageString += "Death"
                    7 -> damageString += "Life"
                }
                damageString += " by ${i.second * 100}%, "
            }
            returnString += damageString.dropLast(2) + "\n"
        }
        if (upgradedResistances.isNotEmpty()) {
            resistancesString += "increases your resistances: "
            for (i in upgradedResistances) {
                when (i.first) {
                    0 -> resistancesString += "Physical"
                    1 -> resistancesString += "Pure mana"
                    2 -> resistancesString += "Fire"
                    3 -> resistancesString += "Water"
                    4 -> resistancesString += "Air"
                    5 -> resistancesString += "Earth"
                    6 -> resistancesString += "Death"
                    7 -> resistancesString += "Life"
                }
                resistancesString += " by ${i.second * 100}%, "
            }
            returnString += resistancesString.dropLast(2) + "\n"
        }
        if (addedSpells.isNotEmpty()) {
            spellsString += "unlocks the following spells: "
            for (i in addedSpells) {
                spellsString += i.name + ", "
            }
            returnString += spellsString.dropLast(2) + "\n"
        }
        return returnString
    }
}