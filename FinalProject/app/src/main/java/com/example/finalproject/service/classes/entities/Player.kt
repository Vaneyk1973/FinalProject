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
    var researchPoints: Int = 100
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
        50,
        0,
        10,
        User(),
        0,
        arrayListOf(Pair(x, y), Pair(6, 3))
    )

    /**
     * @param target the target to be damaged
     * damages the target
     */
    override fun doDamage(target: Health) {
        target.takeDamage(damage)
    }

    /**
     * @param target the target to be damaged
     * @param ref a Firebase reference to a target in the database
     * damages the target
     */
    override fun doDamage(target: Health, ref: DatabaseReference) {
        target.takeDamage(damage, ref)
    }

    /**
     * defends or takes down the defence
     */
    override fun defend() {
        def = !def
        if (def)
            resistances.applyDefence(defCoefficient)
        else
            resistances.removeDefence()
    }

    /**
     * checks the condition and changes the characteristics of a player
     */
    override fun levelUp() {
        while (experience >= experienceToTheNextLevelRequired) {
            level++
            researchPoints += 2
            maxHealth += 10
            maxMana += 2
            health = maxHealth
            mana = maxMana
            experience -= experienceToTheNextLevelRequired
            experienceToTheNextLevelRequired = experienceToTheNextLevelRequiredFormula(level + 1)
        }
    }

    /**
     * @param level the next level
     * @return experience required to level up to the next level
     * calculates the returned value
     */
    private fun experienceToTheNextLevelRequiredFormula(level: Int): Int =
        (2.0.pow(2 * sqrt(level.toDouble())) + level).toInt()

    /**
     * @param item the quantity of items to be added and the item template
     * adds items to the inventory
     */
    override fun addItemsToInventory(item: Pair<Int, Item>) {
        inventory.inventory[item.second.id] =
            (inventory.inventory[item.second.id] ?: 0) + item.first
        assets.itemsObtained[item.second.id] =
            (assets.itemsObtained[item.second.id] ?: 0) + item.first
    }

    /**
     * @param item the quantity of items to be removed and the item template
     * @return if items are removed successfully, false otherwise
     */
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

    /**
     * @param recipe the recipe of an item(s) to be crafted
     * @return true if the item(s) is crafted successfully, false otherwise
     * crafts the items and adds them to the inventory
     */
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

    /**
     * @param loot the entity from which to collect items, gold and experience
     * collects the drop from the entity
     */
    fun takeDrop(loot: Lootable) {
        gold += loot.loot.gold
        experience += loot.loot.exp
        levelUp()
        for (i in loot.loot.dropLoot())
            addItemsToInventory(i)
    }

    /**
     * @param item the item to be equipped
     * @return true if item is equipped successfully, false otherwise
     * adds the item as equipment
     */
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

    /**
     * @param item the item to be unequipped
     * @return true if item is unequipped successfully, false otherwise
     * removes the item from equipment
     */
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

    /**
     * @param playerReference the Firebase reference to an object in database
     * regenerates and changes the value in the database
     */
    override fun regenerate(playerReference: DatabaseReference) {
        super.regenerate(playerReference)
        playerReference.setValue(Json.encodeToString(Enemy.serializer(), Enemy(this, damage)))
    }

    /**
     * @param damage the damage to be taken
     * @param ref Firebase reference to an object in the database
     * takes the damage and changes the value in the database
     */
    override fun takeDamage(damage: Damage, ref: DatabaseReference) {
        super.takeDamage(damage, ref)
        ref.setValue(Json.encodeToString(Enemy.serializer(), Enemy(this, damage)))
    }

    /**
     * @param research the id of a research
     * @return true if researched successfully, false otherwise
     * researches the provided research
     */
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

    /**
     * checks if some of the taken tasks are complete and removes the from the active task list
     */
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

    /**
     * @param item the quantity of items to be sold, the item's id
     * @return true if sold successfully, false otherwise
     * sells the item in the shop
     */
    fun sellItem(item: Pair<Int, Item>): Boolean =
        if (inventory.quantity(item.second.id) >= item.first) {
            gold += item.second.costSell * item.first
            removeItemsFromInventory(item)
            true
        } else {
            false
        }

    /**
     * @param item the quantity of items to be bought, the item's id
     * @return true if bought successfully, false otherwise
     * buys the item in the shop
     */
    fun buyItem(item: Pair<Int, Item>): Boolean =
        if (gold >= item.second.costBuy * item.first) {
            gold -= item.second.costBuy * item.first
            addItemsToInventory(item)
            true
        } else {
            false
        }
}