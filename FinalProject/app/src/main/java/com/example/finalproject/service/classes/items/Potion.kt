package com.example.finalproject.service.classes.items

class Potion(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    rarity: Int,
    category: Int,
    val lastingTime: Int,
    val strength: Double,
    val effect: Int
) :
    Item(name, id, costSell, costBuy, rarity, category)