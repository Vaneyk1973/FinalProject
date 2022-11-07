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

    /**
     * changes the assets in according to the properties
     */
    fun affect() {
        for (research in affectedResearches) {
            if (assets.researches[research]?.enable() == true && assets.researches[research]?.researched == false)
                assets.availableResearches.add(research)
        }
        for (component in unlockedComponents) {
            assets.elements[component]?.avail()
            assets.types[component]?.avail()
            assets.manaChannels[component]?.avail()
            assets.forms[component]?.avail()
            assets.manaReservoirs[component]?.avail()
        }
        for (spell in addedSpells)
            player.spells.add(spell)
        player.resistances.upgradeResistances(upgradedResistances)
        player.damage.upgradeDamage(upgradedDamage)
    }

    /**
     * @return the string value of an object
     */
    override fun toString(): String {
        var returnString = ""
        var componentsString = ""
        var resistancesString = ""
        var damageString = ""
        var spellsString = ""
        if (unlockedComponents.isNotEmpty()) {
            componentsString += "unlocks the following components: "
            for (i in unlockedComponents) {
                 when (i) {
                    in assets.elements.keys -> componentsString += "Element ${assets.elements[i]!!.name}, "
                     in assets.types.keys -> componentsString += "Type ${assets.types[i]!!.name}, "
                     in assets.forms.keys -> componentsString += "Form ${assets.forms[i]!!.name}, "
                     in assets.manaChannels.keys -> componentsString += "Mana Channel ${assets.manaChannels[i]!!.name}, "
                     in assets.manaReservoirs.keys -> componentsString += "Mana Reservoir ${assets.manaReservoirs[i]!!.name}, "
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