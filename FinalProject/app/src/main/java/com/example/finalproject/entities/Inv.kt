package com.example.finalproject.entities

import com.example.finalproject.items.Item

interface Inv {
    val inventory:Inventory

    fun addItemsToInventory(item:Pair<Int, Item>)

    fun removeItemsFromInventory(item:Pair<Int, Item>)

    fun getItemIndex(item:Item)
}