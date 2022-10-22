package com.example.finalproject.service.classes.items

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.serializers.WeaponSerializer
import kotlinx.serialization.Serializable

@Serializable(with = WeaponSerializer::class)
class Weapon(
    name: String,
    id: Int,
    costSell: Int,
    costBuy: Int,
    rarity: Int,
    category: Int,
    val damage: Damage,
    val typeOfWeapon: Int,
) : Item(
    name = name,
    id = id,
    costSell = costSell,
    costBuy = costBuy,
    rarity = rarity,
    category = category
) {
    constructor(item: Item, damage: Damage, typeOfWeapon: Int) : this(
        item.name,
        item.id,
        item.costSell,
        item.costBuy,
        item.rarity,
        item.category,
        damage,
        typeOfWeapon
    )
}