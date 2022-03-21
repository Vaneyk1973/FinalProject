package com.example.finalproject.items

class Food(val manaRecovery:Double, val healthRecovery:Double, val weight:Double,
           costSell:Double, costBuy:Double, category:Int, rarity:Int, name:String):Item(costSell, costBuy, rarity, category, name)