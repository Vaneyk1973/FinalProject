package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable

@Serializable
class ResearchEffect(
    val name: String,
    val id: Int,
    val affectedResearches: ArrayList<Int> = ArrayList(),
    val unlockedComponents: ArrayList<Int> = ArrayList(),
    val upgradedResistances: ArrayList<Int> = ArrayList(),
    val upgradedDamage: ArrayList<Int> = ArrayList()
){
    fun affect(){

    }
}