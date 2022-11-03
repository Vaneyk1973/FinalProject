package com.example.finalproject.service.classes.entities

import android.util.Log
import com.example.finalproject.MainActivity
import com.example.finalproject.service.classes.*
import com.example.finalproject.service.classes.items.Armor
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.items.Weapon
import com.example.finalproject.service.classes.spell.Spell
import com.example.finalproject.service.interfaces.*
import com.example.finalproject.service.serializers.PlayerSerializer
import com.google.firebase.database.DatabaseReference
import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable(with = PlayerSerializer::class)
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
    var mapNumber: Int = 0,
    val coordinates: ArrayList<Pair<Int, Int>> = ArrayList()
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
    var researchPoints: Int = 0
    var chatMode: Boolean = false
    var gold: Int = 0
    private val defCoefficient = 1.05
    override var def: Boolean = false

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
        player.mapNumber,
        player.coordinates
    ) {
        spells.clear()
        spells.addAll(player.spells.clone() as Collection<Spell>)
        researchPoints = player.researchPoints
        chatMode = player.chatMode
        gold = player.gold
    }

    constructor(
        entity: Entity,
        inventory: Inventory,
        damage: Damage,
        equipment: ArrayList<Item>,
        level: Int,
        experience: Int,
        experienceToTheNextLevelRequired: Int,
        user: User,
        mapNumber: Int,
        coordinates: ArrayList<Pair<Int, Int>>,
        spells: ArrayList<Spell>,
        researchPoints: Int,
        chatMode: Boolean,
        gold: Int
    ) : this(
        entity.name,
        entity.id,
        entity.health,
        entity.maxHealth,
        entity.healthRegen,
        entity.mana,
        entity.maxMana,
        entity.manaRegen,
        entity.resistances,
        entity.loot,
        inventory,
        damage,
        equipment,
        level,
        experience,
        experienceToTheNextLevelRequired,
        user,
        mapNumber,
        coordinates
    ) {
        this.spells.clear()
        this.spells.addAll(spells.clone() as ArrayList<Spell>)
        this.researchPoints = researchPoints
        this.chatMode = chatMode
        this.gold = gold
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
        arrayListOf(0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        Loot(),
        Inventory(),
        Damage(arrayListOf(4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
        ArrayList(),
        1,
        0,
        10,
        User(),
        0,
        arrayListOf(Pair(x, y), Pair(6, 3))
    )

    override fun doDamage(target: Health) {
        target.takeDamage(damage)
    }

    override fun doDamage(target: Health, ref: DatabaseReference) {
        target.takeDamage(damage, ref)
    }

    override fun defend() {
        def = !def
        if (def)
            for (i in 0 until resistances.size)
                resistances[i] *= defCoefficient
        else
            for (i in 0 until resistances.size)
                resistances[i] /= defCoefficient
    }

    override fun levelUp() {
        while (experience >= experienceToTheNextLevelRequired) {
            level++
            researchPoints += 1
            maxHealth += 10
            maxMana += 2
            health = maxHealth
            mana = maxMana
            experience -= experienceToTheNextLevelRequired
            experienceToTheNextLevelRequired = experienceToTheNextLevelRequiredFormula(level + 1)
        }
    }

    private fun experienceToTheNextLevelRequiredFormula(level: Int): Int =
        (2.0.pow(2 * sqrt(level.toDouble())) + level).toInt()

    override fun addItemsToInventory(item: Pair<Int, Item>) {
        inventory.inventory[item.second.id] =
            (inventory.inventory[item.second.id] ?: 0) + item.first
        MainActivity.assets.itemsObtained[item.second.id] =
            (MainActivity.assets.itemsObtained[item.second.id] ?: 0) + item.first
    }

    override fun removeItemsFromInventory(item: Pair<Int, Item>): Boolean {
        if (inventory.quantity(item.second.id) > item.first) {
            inventory.inventory[item.second.id] = inventory.inventory[item.second.id]!! - item.first
            return true
        } else if (inventory.quantity(item.second.id) == item.first) {
            inventory.removeItem(item.second.id)
            return true
        } else {
            Log.d("InventoryError: ", "Cannot remove items. Not enough items in the inventory")
            return false
        }
    }

    fun craft(recipe: Recipe): Boolean {
        for (item in recipe.ingredients) {
            if (inventory.quantity(item.second.id) < item.first) {
                Log.d(
                    "InventoryError: ",
                    "Cannot craft items. Not enough ingredients in the inventory"
                )
                return false
            }
        }
        for (item in recipe.ingredients)
            removeItemsFromInventory(item)
        addItemsToInventory(recipe.product)
        return true
    }

    fun takeDrop(loot: Lootable) {
        gold += loot.loot.gold
        experience += loot.loot.exp
        levelUp()
        for (i in loot.loot.dropLoot())
            addItemsToInventory(i)
    }

    override fun equipItem(item: Item): Boolean =
        when (item) {
            is Weapon -> {
                equipment[0] = item
                true
            }

            is Armor -> {
                equipment[item.typeOfArmor] = item
                true
            }

            else -> {
                false
            }
        }

    override fun unequipItem(item: Item): Boolean {
        return if (item is Weapon && equipment[0] == item) {
            equipment[0] = Item()
            true
        } else if (item is Armor && equipment[item.typeOfArmor] == item) {
            equipment[item.typeOfArmor] = Item()
            true
        } else {
            false
        }
    }

    fun research(research: Research): Boolean {
        return if (research.cost > researchPoints)
            false
        else {
            researchPoints -= research.cost
            research.research()
        }
    }

    fun checkTasks() {
        val newActiveTasks = ArrayList<Int>()
        for (task in MainActivity.assets.activeTasks) {
            if (MainActivity.assets.tasks[task]?.checkTask() != true) {
                newActiveTasks.add(task)
            }
        }
        MainActivity.assets.activeTasks.clear()
        MainActivity.assets.activeTasks.addAll(newActiveTasks)
    }

    fun sellItem(item: Pair<Int, Item>): Boolean =
        if (inventory.quantity(item.second.id) >= item.first) {
            gold += item.second.costBuy * item.first
            removeItemsFromInventory(item)
            true
        } else {
            false
        }

    fun buyItem(item: Pair<Int, Item>): Boolean =
        if (gold >= item.second.costSell * item.first) {
            gold -= item.second.costSell * item.first
            addItemsToInventory(item)
            true
        } else {
            false
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