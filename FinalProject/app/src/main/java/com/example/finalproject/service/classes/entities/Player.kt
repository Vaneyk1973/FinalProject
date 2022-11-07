package com.example.finalproject.service.classes.entities

import android.util.Log
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Inventory
import com.example.finalproject.service.classes.Loot
import com.example.finalproject.service.classes.Recipe
import com.example.finalproject.service.classes.Resistances
import com.example.finalproject.service.classes.User
import com.example.finalproject.service.classes.items.Armor
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.items.Weapon
import com.example.finalproject.service.classes.spell.Spell
import com.example.finalproject.service.interfaces.Dmg
import com.example.finalproject.service.interfaces.Equipment
import com.example.finalproject.service.interfaces.Health
import com.example.finalproject.service.interfaces.Inv
import com.example.finalproject.service.interfaces.Level
import com.example.finalproject.service.interfaces.Lootable
import com.example.finalproject.service.serializers.PlayerSerializer
import com.google.firebase.database.DatabaseReference
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
    resistances: Resistances,
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
), Dmg, Level, Inv, Equipment {
    val spells: ArrayList<Spell> = ArrayList()
    var researchPoints: Int = 1000
    var chatMode: Boolean = false
    var gold: Int = 0
    private val defCoefficient = 1.05
    override var def: Boolean = false

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
        511,
        40.0,
        40.0,
        1.0,
        10.0,
        10.0,
        0.1,
        Resistances(),
        Loot(),
        Inventory(),
        Damage(arrayListOf(4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
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
            resistances.applyDefence(defCoefficient)
        else
            resistances.removeDefence(defCoefficient)
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
        assets.itemsObtained[item.second.id] =
            (assets.itemsObtained[item.second.id] ?: 0) + item.first
    }

    override fun removeItemsFromInventory(item: Pair<Int, Item>): Boolean {
        return if (inventory.quantity(item.second.id) > item.first) {
            inventory.inventory[item.second.id] = inventory.inventory[item.second.id]!! - item.first
            true
        } else if (inventory.quantity(item.second.id) == item.first) {
            inventory.removeItem(item.second.id)
            true
        } else {
            Log.d("InventoryError: ", "Cannot remove items. Not enough items in the inventory")
            false
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

    override fun regenerate(playerReference: DatabaseReference) {
        super.regenerate(playerReference)
        playerReference.setValue(Json.encodeToString(Enemy.serializer(), Enemy(this, damage)))
    }

    override fun takeDamage(damage: Damage, ref: DatabaseReference) {
        super.takeDamage(damage, ref)
        ref.setValue(Json.encodeToString(Enemy.serializer(), Enemy(this, damage)))
    }

    fun research(research: Int): Boolean {
        if (assets.researches[research] != null) {
            if (researchPoints >= assets.researches[research]!!.cost) {
                if (assets.researches[research]!!.research()) {
                    researchPoints -= assets.researches[research]!!.cost
                    return true
                }
            }
        }
        return false
    }

    fun checkTasks() {
        val newActiveTasks = ArrayList<Int>()
        for (task in assets.activeTasks) {
            if (assets.tasks[task]?.checkTask() != true) {
                newActiveTasks.add(task)
            }
        }
        assets.activeTasks.clear()
        assets.activeTasks.addAll(newActiveTasks)
    }

    fun sellItem(item: Pair<Int, Item>): Boolean =
        if (inventory.quantity(item.second.id) >= item.first) {
            gold += item.second.costSell * item.first
            removeItemsFromInventory(item)
            true
        } else {
            false
        }

    fun buyItem(item: Pair<Int, Item>): Boolean =
        if (gold >= item.second.costBuy * item.first) {
            gold -= item.second.costBuy * item.first
            addItemsToInventory(item)
            true
        } else {
            false
        }
}