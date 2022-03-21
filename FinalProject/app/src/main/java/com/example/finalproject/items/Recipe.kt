package com.example.finalproject.items

import com.example.finalproject.entities.Player

class Recipe(val product:Pair<Item, Int>, private val ingredients:ArrayList<Pair<Item, Int>>){

    init {
        this.ingredients.clear()
        this.ingredients.addAll(ingredients)
    }

    @JvmName("getIngredients1")
    fun getIngredients():ArrayList<Pair<Item, Int>>{
        return ArrayList(ingredients)
    }

    @JvmName("getProduct1")
    fun getProduct():Pair<Item, Int>{
        return Pair(product.first.clone(), product.second)
    }
}