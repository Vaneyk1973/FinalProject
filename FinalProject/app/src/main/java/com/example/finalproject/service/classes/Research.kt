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
    var researched: Boolean,
    var available: Boolean,
    val requiredResearches: ArrayList<Int>
) {

    private fun enable() {
        for (i in requiredResearches)
            if (assets.researches[i]?.researched != true)
                return
        available = true
    }

    fun research(): Boolean {
        if (available)
            researched = true
        return researched
    }
}