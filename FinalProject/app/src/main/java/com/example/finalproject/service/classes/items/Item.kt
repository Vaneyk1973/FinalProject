package com.example.finalproject.service.classes.items

import com.example.finalproject.service.classes.Unit
import com.example.finalproject.service.serializers.ItemSerializer
import kotlinx.serialization.Serializable

@Serializable(with= ItemSerializer::class)
open class Item(
    name: String,
    id: Int,
    val costSell: Int,
    val costBuy: Int,
    val rarity: Int,
    val category: Int
) : Unit(name = name, id = id) {

    constructor() : this("", -1, -1, -1, -1, -1)

    constructor(item: Item) : this(
        item.name,
        item.id,
        item.costSell,
        item.costBuy,
        item.rarity,
        item.category
    )
}