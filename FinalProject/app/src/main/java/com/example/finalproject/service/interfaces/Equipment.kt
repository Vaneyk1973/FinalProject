package com.example.finalproject.service.interfaces

import com.example.finalproject.service.classes.items.Item

interface Equipment {
    val equipment:ArrayList<Item>

    fun equipItem(item: Item): Boolean

    fun unequipItem(item: Item): Boolean
}