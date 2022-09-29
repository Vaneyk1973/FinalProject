package com.example.finalproject.entities

import com.example.finalproject.fragments.InventoryFragment
import com.example.finalproject.items.Item

@Suppress("UNCHECKED_CAST")

class Inventory() {
    val inventory: ArrayList<Pair<Int, Item>>
        get() = inventory.clone() as ArrayList<Pair<Int, Item>>

    constructor(inventory:ArrayList<Pair<Int, Item>>):this(){
        for (item in inventory)
            this.inventory.add(item)
    }

    fun index(item:Item): Int {
        for(i in inventory.indices)
            if (inventory[i].second == item)
                return i
        return -1
    }

    fun quantity(item: Item): Int {
        for(i in inventory)
            if (i.second == item)
                return i.first
        return 0
    }
}