package com.example.finalproject.service.classes.entities

import com.example.finalproject.fragments.MainActivity.Companion.player
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
        this.spells.addAll(spells.clone() as Collection<Spell>)
        this.researchPoints = player.researchPoints
        this.chatMode = player.chatMode
        this.gold = player.gold
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
        Loot(),
        Inventory(),
        Damage(),
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

    override fun defend(def: Boolean) {
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
            experience -= experienceToTheNextLevelRequired
            experienceToTheNextLevelRequired = experienceToTheNextLevelRequiredFormula(level + 1)
        }
    }

    private fun experienceToTheNextLevelRequiredFormula(level: Int): Int =
        (2.0.pow(2 * sqrt(level.toDouble())) + level).toInt()

    override fun addItemsToInventory(item: Pair<Int, Item>) {
        if (inventory.quantity(item.second) > 0) {
            val itemIndex = inventory.index(item.second)
            inventory.inventory[itemIndex] =
                Pair(item.first + inventory.inventory[itemIndex].first, item.second)
        } else {
            inventory.inventory.add(item)
        }
    }

    override fun removeItemsFromInventory(item: Pair<Int, Item>): Boolean {
        return if (inventory.quantity(item.second) > item.first) {
            val itemIndex = inventory.index(item.second)
            inventory.inventory[itemIndex] =
                Pair(item.first - inventory.inventory[itemIndex].first, item.second)
            true
        } else if (inventory.quantity(item.second) == item.first) {
            inventory.inventory.remove(item)
            return true
        } else {
            false
        }
    }

    fun craft(recipe: Recipe): Boolean {
        for (i in recipe.ingredients)
            if (inventory.quantity(i.second) == 0)
                return false
        for (i in recipe.ingredients)
            removeItemsFromInventory(i)
        addItemsToInventory(recipe.product)
        return true
    }

    fun takeDrop(loot: Lootable) {
        for (i in loot.loot.dropLoot())
            addItemsToInventory(i)
    }

    override fun equipItem(item: Item): Boolean {
        return when (item) {
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

    fun research(research: Research): Boolean = research.research()

    fun checkTasks() {
        TODO("Not yet implemented")
    }

    fun sellItem(item: Pair<Int, Item>): Boolean {
        return if (inventory.quantity(item.second) >= item.first) {
            gold += item.second.costSell * item.first
            removeItemsFromInventory(item)
            true
        } else {
            false
        }
    }

    fun buyItem(item: Pair<Int, Item>): Boolean {
        return if (gold >= item.second.costSell * item.first) {
            gold -= item.second.costSell * item.first
            addItemsToInventory(item)
            true
        } else {
            false
        }
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