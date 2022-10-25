package com.example.finalproject.service.classes

import com.example.finalproject.service.classes.items.Item
import kotlinx.serialization.Serializable

@Serializable
class Inventory() {

    val inventory: ArrayList<Pair<Int, Item>> = ArrayList()

    constructor(inventory: ArrayList<Pair<Int, Item>>) : this() {
        for (item in inventory)
            this.inventory.add(item)
    }


    fun index(item: Item): Int {
        for (i in inventory.indices)
            if (inventory[i].second == item)
                return i
        return -1
    }

    fun quantity(item: Item): Int {
        for (i in inventory)
            if (i.second == item)
                return i.first
        return 0
    }

    fun removeItem(item: Item) {
        inventory.remove(inventory[index(item)])
    }
}