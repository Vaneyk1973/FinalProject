package com.example.finalproject.items

open class Item(val costSell:Double, val costBuy:Double, val rarity:Int, val category:Int, val name:String) {

    constructor():this(0.0, 0.0, 0, 0, "")

    constructor(item: Item):this(item.costSell, item.costBuy, item.rarity, item.category, item.name)

    open fun clone():Item{
        return Item(costSell, costBuy, rarity, category, name)
    }
}