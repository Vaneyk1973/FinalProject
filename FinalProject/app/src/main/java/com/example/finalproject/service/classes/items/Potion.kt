package com.example.finalproject.service.classes.items

import com.example.finalproject.service.serializers.PotionSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PotionSerializer::class)
class Potion(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    rarity: Int,
    category: Int,
    val lastingTime: Int,
    val effects: ArrayList<Int>,
    val strengths: ArrayList<Double>
) :
    Item(
        name = name,
        id = id,
        costSell = costSell,
        costBuy = costBuy,
        rarity = rarity,
        category = category
    ) {
    constructor(
        item: Item,
        lastingTime: Int,
        effects: ArrayList<Int>,
        strengths: ArrayList<Double>
    ) : this(
        item.name,
        item.id,
        item.costSell,
        item.costBuy,
        item.rarity,
        item.category,
        lastingTime,
        effects,
        strengths
    )
}