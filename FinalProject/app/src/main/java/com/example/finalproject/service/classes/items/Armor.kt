package com.example.finalproject.service.classes.items

class Armor(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    category: Int,
    rarity: Int,
    val armor: Double,
    val weight: Double,
    val typeOfArmor: Int
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
)
