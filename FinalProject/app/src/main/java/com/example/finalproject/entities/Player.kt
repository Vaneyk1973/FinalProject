package com.example.finalproject.entities

import com.example.finalproject.items.Item
import com.google.firebase.database.DatabaseReference

class Player(
    name: String,
    id: Int,
    health: Double,
    resistances: ArrayList<Double>,
    loot: Loot,
    override val inventory: Inventory,
    override var damage: Damage,
    override var mana: Double
) : Entity(name = name, id = id, health = health, resistances = resistances, loot = loot), Health,
    Dmg, Mana, Level, Inv {

    override fun doDamage(target: Health) {
        TODO("Not yet implemented")
    }

    override fun doDamage(target: Health, ref: DatabaseReference) {
        TODO("Not yet implemented")
    }

    override fun attack(target: Health) {
        TODO("Not yet implemented")
    }

    override fun defend() {
        TODO("Not yet implemented")
    }

    override fun startFight() {
        TODO("Not yet implemented")
    }

    override fun levelUp() {
        TODO("Not yet implemented")
    }

    override fun castSpell(target: Health) {
        TODO("Not yet implemented")
    }

    override fun addItemsToInventory(item: Pair<Int, Item>) {
        TODO("Not yet implemented")
    }

    override fun removeItemsFromInventory(item: Pair<Int, Item>) {
        TODO("Not yet implemented")
    }

    override fun getItemIndex(item: Item) {
        TODO("Not yet implemented")
    }
}