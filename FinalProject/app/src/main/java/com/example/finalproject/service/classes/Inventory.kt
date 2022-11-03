package com.example.finalproject.service.classes

import com.example.finalproject.service.classes.items.Item
import kotlinx.serialization.Serializable

@Serializable
class Inventory {

    val inventory: HashMap<Int, Int> = HashMap() //<is of an item, quantity>

    fun quantity(item: Int): Int {
        return inventory[item] ?: -1
    }

    fun contains(item: Int): Boolean = inventory[item] != null

    fun removeItem(item: Int) {
        inventory.keys.remove(item)
    }
}