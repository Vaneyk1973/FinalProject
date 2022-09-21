package com.example.finalproject.entities

import com.example.finalproject.items.Item

@Suppress("UNCHECKED_CAST")

class Inventory {
    val inventory: ArrayList<Pair<Int, Item>>
        get() = inventory.clone() as ArrayList<Pair<Int, Item>>

    fun index(item:Item){

    }
}