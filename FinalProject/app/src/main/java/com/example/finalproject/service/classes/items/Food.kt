package com.example.finalproject.service.classes.items

import com.example.finalproject.service.serializers.FoodSerializer
import kotlinx.serialization.Serializable

@Serializable(with = FoodSerializer::class)
class Food(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    category: Int,
    rarity: Int,
    val manaRecovery: Double,
    val healthRecovery: Double,
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
) {
    constructor(item: Item, manaRecovery: Double, healthRecovery: Double) : this(
        item.name,
        item.id,
        item.costSell,
        item.costBuy,
        item.rarity,
        item.category,
        manaRecovery,
        healthRecovery
    )
}