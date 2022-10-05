package com.example.finalproject.service.interfaces

import com.example.finalproject.service.classes.items.Item
import com.google.firebase.database.DatabaseReference

interface Auction {

    fun buyItemOnAuction(item:Pair<Int, Item>, ref:DatabaseReference)

    fun putItemOnAuction(item: Pair<Int, Item>, ref: DatabaseReference)

    fun removeItemFromAuction(item: Pair<Int, Item>, ref: DatabaseReference)
}