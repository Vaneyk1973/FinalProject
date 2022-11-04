package com.example.finalproject.service.classes

import com.example.finalproject.MainActivity.Companion.assets
import kotlinx.serialization.Serializable

@Serializable
data class Research(
    val name: String,
    val id: Int,
    val cost: Int,
    val tier: Int,
    val effect: Int,
    var researched: Boolean = false,
    var available: Boolean = false,
    val requiredResearches: ArrayList<Int> = ArrayList(),
    var description: String = ""
) {
    fun enable(): Boolean {
        for (i in requiredResearches)
            if (assets.researches[i]?.researched != true)
                return false
        available = true
        return true
    }

    fun research(): Boolean {
        if (available) {
            researched = true
            available = false
            assets.availableResearches.remove(id)
            assets.researchEffects[effect]?.affect()
        }
        return researched
    }
}