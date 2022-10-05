package com.example.finalproject.service.classes.items

class Food(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    category: Int,
    rarity: Int,
    val manaRecovery: Double,
    val healthRecovery: Double,
    val weight: Double,
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
)