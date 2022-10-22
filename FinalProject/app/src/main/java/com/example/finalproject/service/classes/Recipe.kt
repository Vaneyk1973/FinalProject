package com.example.finalproject.service.classes

import com.example.finalproject.service.classes.items.Item
import kotlinx.serialization.Serializable

@Serializable
class Recipe(
    val product: Pair<Int, Item> = Pair(0, Item()),
    val ingredients: ArrayList<Pair<Int, Item>> = ArrayList()
)