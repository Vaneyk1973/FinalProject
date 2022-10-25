package com.example.finalproject.service.classes

import com.example.finalproject.MainActivity
import kotlinx.serialization.Serializable

@Serializable
data class Research(
    val name: String,
    val cost: Int,
    val tier: Int,
    val effect: Int,
    var researched: Boolean,
    var available: Boolean,
    val requiredResearches: ArrayList<Research>
) {

    private fun affect() = MainActivity.assets.elements[effect].avail()

    private fun enable() {
        for (i in requiredResearches)
            if (!i.researched) return
        available = true
    }

    fun research(): Boolean {
        if (available)
            researched = true
        return researched
    }
}