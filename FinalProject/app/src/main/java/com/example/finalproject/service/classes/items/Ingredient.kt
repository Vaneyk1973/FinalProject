package com.example.finalproject.service.classes.items

import com.example.finalproject.service.serializers.IngredientSerializer
import kotlinx.serialization.Serializable

@Serializable(with = IngredientSerializer::class)

class Ingredient(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    rarity: Int,
    category: Int,
    val effect: Int,
    val effectStrength: Double
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
) {
    constructor(item: Item, effect: Int, effectStrength: Double) : this(
        item.name,
        item.id,
        item.costSell,
        item.costBuy,
        item.rarity,
        item.category,
        effect,
        effectStrength
    )
}