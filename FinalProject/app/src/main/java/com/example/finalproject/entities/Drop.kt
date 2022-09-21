package com.example.finalproject.entities

import com.example.finalproject.items.Item

interface Drop {
    val loot:Loot

    fun dropLoot():ArrayList<Pair<Int, Item>> 
}