package com.example.finalproject.service.classes

import kotlinx.serialization.Serializable

@Serializable
class Inventory {

    val inventory: HashMap<Int, Int> = HashMap() //<is of an item, quantity>

    /**
     * @param item id of an item
     * @return the quantity of that item in the inventory
     */
    fun quantity(item: Int): Int {
        return inventory[item] ?: -1
    }

    /**
     * @param item id of an item
     * @return true if item exists and is contained in the inventory, false otherwise
     */
    fun contains(item: Int): Boolean = inventory[item] != null

    /**
     * @param item id of an item
     * removes the item from the inventory
     */
    fun removeItem(item: Int) {
        inventory.keys.remove(item)
    }
}