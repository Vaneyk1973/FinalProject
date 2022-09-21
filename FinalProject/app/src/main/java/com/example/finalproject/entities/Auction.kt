package com.example.finalproject.entities

import com.example.finalproject.items.Item
import com.google.firebase.database.DatabaseReference

interface Auction {

    fun buyItemOnAuction(item:Pair<Int, Item>, ref:DatabaseReference)

    fun putItemOnAuction(item: Pair<Int, Item>, ref: DatabaseReference)

    fun removeItemFromAuction(item: Pair<Int, Item>, ref: DatabaseReference)
}