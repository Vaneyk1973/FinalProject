package com.example.finalproject.service.classes.items

import com.example.finalproject.service.serializers.ArmorSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ArmorSerializer::class)
class Armor(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    category: Int,
    rarity: Int,
    val armor: Double,
    val typeOfArmor: Int
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
) {
    constructor(item: Item, armor: Double, typeOfArmor: Int) : this(
        item.name,
        item.id,
        item.costSell,
        item.costBuy,
        item.rarity,
        item.category,
        armor,
        typeOfArmor
    )
}
