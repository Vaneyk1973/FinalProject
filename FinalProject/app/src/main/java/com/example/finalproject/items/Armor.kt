package com.example.finalproject.items

class Armor(name:String, costSell:Double, costBuy:Double,
            val armor:Double, val weight:Double, val typeOfArmor:Int,
            category:Int, rarity:Int)
    :Item(costSell, costBuy, category, rarity, name){
        constructor(armor: Armor):this(armor.name, armor.costSell, armor.costBuy,
            armor.armor, armor.weight,
            armor.typeOfArmor, armor.category, armor.rarity)
    }
