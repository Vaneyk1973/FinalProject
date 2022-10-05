package com.example.finalproject.entities

import com.example.finalproject.items.Item
import com.example.finalproject.items.Recipe
import com.example.finalproject.service.Research
import com.example.finalproject.service.User
import com.example.finalproject.service.spell.Spell
import com.google.firebase.database.DatabaseReference
import kotlin.math.max

class Player(
    name: String,
    id: Int,
    health: Double,
    maxHealth: Double,
    healthRegen: Double,
    mana: Double,
    maxMana: Double,
    manaRegen: Double,
    resistances: ArrayList<Double>,
    loot: Loot,
    override val inventory: Inventory,
    override var damage: Damage,
    override val equipment: ArrayList<Item>,
    override var level: Int,
    override var experience: Int,
    override var experienceToTheNextLevelRequired: Int,
    var user: User,
    var mapNum: Int = 0,
    val coordinates: ArrayList<Pair<Int, Int>> = ArrayList(2)
) : Entity(
    name = name,
    id = id,
    health = health,
    maxHealth = maxHealth,
    healthRegen = healthRegen,
    mana = mana,
    maxMana = maxMana,
    manaRegen = manaRegen,
    resistances = resistances,
    loot = loot
), Dmg, Level, Inv, Equipment, Auction {
    val spells: ArrayList<Spell> = ArrayList()
    var researchPoints:Int=0
    var chatMode:Boolean=false
    var gold:Int=0

    @Suppress("UNCHECKED_CAST")
    constructor(player: Player) : this(
        player.name,
        player.id,
        player.health,
        player.maxHealth,
        player.healthRegen,
        player.mana,
        player.maxMana,
        player.manaRegen,
        player.resistances,
        player.loot,
        player.inventory,
        player.damage,
        player.equipment,
        player.level,
        player.experience,
        player.experienceToTheNextLevelRequired,
        player.user,
        player.mapNum,
        player.coordinates
    ){
        spells.clear()
        spells.addAll(player.spells.clone() as Collection<Spell>)
        researchPoints=player.researchPoints
        chatMode=player.chatMode
        gold=player.gold
    }

    constructor(x: Int, y: Int) : this(
        "Player",
        0,
        100.0,
        100.0,
        1.0,
        10.0,
        10.0,
        0.1,
        ArrayList(),
        Loot(ArrayList()),
        Inventory(ArrayList()),
        Damage(ArrayList()),
        ArrayList(),
        0,
        0,
        10,
        User(),
        0,
        ArrayList()
    )

    override fun doDamage(target: Health) {
        target.takeDamage(damage)
    }

    override fun doDamage(target: Health, ref: DatabaseReference) {
        target.takeDamage(damage, ref)
    }

    override fun defend() {
        TODO("Not yet implemented")
    }

    override fun levelUp() {
        TODO("Not yet implemented")
    }

    override fun addItemsToInventory(item: Pair<Int, Item>) {
        TODO("Not yet implemented")
    }

    override fun removeItemsFromInventory(item: Pair<Int, Item>) {
        TODO("Not yet implemented")
    }

    fun craft(recipe: Recipe): Boolean {
        TODO("Not yet implemented")
    }

    fun takeDrop(drop: Drop) {
        TODO("Not yet implemented")
    }

    override fun equipItem(item: Item) {
        TODO("Not yet implemented")
    }

    override fun unequipItem(item: Item) {
        TODO("Not yet implemented")
    }

    fun research(research: Research): Boolean{
        return research.research()
    }

    fun checkTasks(){
        TODO("Not yet implemented")
    }

    fun sellItem(item:Pair<Int, Item>):Boolean {
        return false
    }

    fun buyItem(item:Pair<Int, Item>):Boolean {
        return false
    }

    override fun buyItemOnAuction(item: Pair<Int, Item>, ref: DatabaseReference) {
        TODO("Not yet implemented")
    }

    override fun putItemOnAuction(item: Pair<Int, Item>, ref: DatabaseReference) {
        TODO("Not yet implemented")
    }

    override fun removeItemFromAuction(item: Pair<Int, Item>, ref: DatabaseReference) {
        TODO("Not yet implemented")
    }
}