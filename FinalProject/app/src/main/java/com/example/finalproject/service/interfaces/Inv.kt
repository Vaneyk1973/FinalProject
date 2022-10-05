package com.example.finalproject.service.interfaces

import com.example.finalproject.service.classes.Inventory
import com.example.finalproject.service.classes.items.Item

interface Inv {
    val inventory: Inventory

    fun addItemsToInventory(item:Pair<Int, Item>)

    fun removeItemsFromInventory(item:Pair<Int, Item>): Boolean
}