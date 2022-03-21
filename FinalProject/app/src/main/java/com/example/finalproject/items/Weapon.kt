package com.example.finalproject.items

class Weapon(val damage:Double, val typeOfDamage:Int, val typeOfWeapon:Int,
             costSell:Double, costBuy:Double, rarity:Int, category:Int, name:String)
    :Item(costSell, costBuy, rarity, category, name){
        constructor(weapon: Weapon):this(weapon.damage, weapon.typeOfDamage, weapon.typeOfWeapon,
            weapon.costSell, weapon.costBuy,
            weapon.rarity, weapon.category, weapon.name)
    }