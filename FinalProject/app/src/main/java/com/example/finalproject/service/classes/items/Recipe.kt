package com.example.finalproject.service.classes.items

class Recipe(
    val product: Pair<Int, Item> = Pair(0, Item()),
    val ingredients: ArrayList<Pair<Int, Item>> = ArrayList()
)