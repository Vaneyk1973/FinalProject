package com.example.finalproject.service.classes.items

class Weapon(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    rarity: Int,
    category: Int,
    val damage: Double,
    val typeOfDamage: Int,
    val typeOfWeapon: Int,
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
)