package com.example.finalproject.entities

import com.example.finalproject.items.Item

interface Equipment {
    val equipment:ArrayList<Item>

    fun equipItem(item: Item)

    fun unequipItem(item: Item)
}