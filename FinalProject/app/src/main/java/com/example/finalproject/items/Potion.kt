package com.example.finalproject.items

class Potion(val lastingTime:Int, val strength:Double, val effect:Int,
             name:String, costSell:Double, costBuy:Double, rarity:Int, category:Int)
    :Item(costSell, costBuy, rarity, category, name){

        override fun clone():Potion {
            return Potion(lastingTime, strength, effect, name, costSell, costBuy, rarity, category)
        }
}