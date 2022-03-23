package com.example.finalproject.entities

import android.graphics.Bitmap
import com.example.finalproject.fragments.MainActivity
import com.example.finalproject.items.*
import com.example.finalproject.service.Research
import com.example.finalproject.service.User
import com.example.finalproject.service.spell.Spell
import java.lang.Math.cbrt
import kotlin.math.*
import kotlin.random.Random

class Player():Entity() {
    var gold:Double=0.0
    var mapNum:Int=0
    var duelNum:Int=0
    var duelPlayerNum:Int=0
    var researchPoints:Int=0
    val coordinates:ArrayList<Pair<Int, Int>> =ArrayList(2)
    val inventory:ArrayList<Pair<Item, Int>> =ArrayList()
    val elementBonuses:ArrayList<Double> =ArrayList()
    val spells:ArrayList<Spell> =ArrayList()
    val itemsOnAuction:HashMap<Int, Int> =HashMap()
    val equipment:ArrayList<Item?> =ArrayList(6)
    val user:User= User("", "")
    var chosenSpell:Spell?=null
    var chatMode:Boolean=false
    private lateinit var titleTexture:Bitmap
    private lateinit var avatar:Bitmap
    var enemy:Enemy?=null

    constructor(x:Int, y:Int):this() {
        healthRegen=3.0
        manaRegen=0.3
        damage=2.0
        level=1
        experience=0.0
        health=50.0
        mana=10.0
        maxHealth=health
        maxMana=mana
        powerLevel=0
        experienceToNextLevelRequired=expFormula(1.0)
        coordinates.add(Pair(x, y))
        coordinates.add(Pair(6, 3))
        for (i in 0 until 6)
            equipment[i]=null
    }

    private fun expFormula(lvl:Double)=(lvl.pow(3.14))* cbrt(lvl.pow(2.7))/ lvl.pow(0.39)

    private fun addExperience(exp:Double){
        experience+=exp
        levelUp()
    }

    private fun addGold(gld:Double){
        gold+=gld
        checkTasks()
    }

    private fun addHealth(health:Double){
        this.health=min(this.health+health, maxHealth)
    }

    private fun addMana(mana:Double){
        this.mana=min(this.mana+mana, maxMana)
    }

    private fun levelUp(){
        while (experience>=experienceToNextLevelRequired){
            level++
            experience-=experienceToNextLevelRequired
            researchPoints+=level%3
            maxMana*=1.3
            mana=maxMana
            maxHealth*=1.3
            health=maxHealth
            manaRegen*=1.4
            healthRegen*=1.4
            checkTasks()
        }
    }

    fun checkTasks(){
        if (gold >= 100 && MainActivity.tasks[0].taken && !MainActivity.tasks[0].completed) {
            MainActivity.tasks[0].completed = true
            addGold(50.0)
            addExperience(50.0)
        }
        if (level >= 5 && MainActivity.tasks[1].taken && !MainActivity.tasks[1].completed) {
            MainActivity.tasks[1].completed = true
            addGold(500.0)
            addExperience(100.0)
        }
    }

    fun research(research: Research):Boolean{
        if (research.available&&!research.researched&&research.cost<=researchPoints){
            research.research()
            researchPoints-=research.cost
            return true
        }
        return false
    }

    fun takeDrop(){
        for (i in enemy!!.drop){
            if (Random(1).nextInt(100)<=i.second){
                val drop:Item=i.first
                if (inventoryContainsItem(drop)){
                    val a:Int=getItemIndex(drop)
                    inventory[a]=Pair(drop, inventory[a].second+i.third)
                } else{
                    inventory.add(Pair(drop, i.third))
                }
            }
        }
        addGold(enemy!!.givenGold)
        addExperience(enemy!!.givenExp)
    }

    private fun inventoryContainsItem(item:Item):Boolean{
        for (i in inventory){
            if (i.first.equals(item))
                return true
        }
        return false
    }

    private fun getItemIndex(item:Item):Int{
        for (i in 0 until inventory.size){
            if (inventory[i].first.equals(item))
                return i
        }
        return -1
    }

    private fun addItem(item: Pair<Item, Int>){
        if (inventoryContainsItem(item.first))
            inventory[getItemIndex(item.first)]= Pair(item.first.clone(), inventory[getItemIndex(item.first)].second+item.second)
        else inventory.add(Pair(item.first.clone(), item.second))
    }

    private fun removeItem(item:Pair<Item, Int>):Boolean{
        val index=getItemIndex(item.first)
        if (index!=-1){
            when {
                inventory[index].second<item.second -> return false
                inventory[index].second==item.second -> inventory.removeAt(index)
                else -> inventory[index]= Pair(item.first.clone(), inventory[index].second-item.second)
            }
            return true
        }
        return false
    }

    fun castSpell()= enemy?.let { chosenSpell?.affect(it) }

    fun chooseSpell(spell: Spell){chosenSpell=Spell(spell)}

    fun equip(item:Item){
        if (item is Weapon)
            equip(item)
        else if(item is Armor)
            equip(item)
    }

    private fun equip(armor:Armor){
        if (equipment[armor.typeOfArmor]!=null){
            this.armor-=(equipment[armor.typeOfArmor]as Armor).armor
            equipment[armor.typeOfArmor]=Armor(armor)
            this.armor+=armor.armor
        }
    }

    private fun equip(weapon: Weapon){equipment[0]=Weapon(weapon)}

    fun craft(recipe: Recipe):Boolean{
        val ingredients:ArrayList<kotlin.Pair<Item, Int>> =recipe.getIngredients()
        for (i in ingredients){
            if (!inventoryContainsItem(i.first)||
                    inventory[getItemIndex(i.first)].second<i.second)
                        return false
        }
        addItem(recipe.getProduct())
        for (i in ingredients)
            removeItem(i)
        return true
    }

    fun eat(food: Food){
        addHealth(food.healthRecovery)
        addMana(food.manaRecovery)
    }

    fun attack(){
        enemy?.takeDamage(damage)
        //TODO duel
    }


    //TODO
    fun addToDuel(){

    }

    fun buyItemOnAuction(){}

    fun placeItemOnAuction(){}

    override fun toString():String{
        return "Player{gold=$gold, experince=$experience, " +
                "researchPoints=$researchPoints, elementBonuses=${elementBonuses.toString()}, " +
                "coordinates=$coordinates, chosenSpell=${chosenSpell.toString()}, equipment=${equipment.toString()}, " +
                "inventory=${inventory.toString()}, spells=${spells.toString()}, enemy=${enemy.toString()}}"
    }

    fun sellItem(item:Pair<Item, Int>):Boolean{
        if (removeItem(item)){
            addGold(item.first.costSell)
            return true
        }
        return false
    }

    fun buyItem(item:Pair<Item, Int>):Boolean{
        if (gold<item.first.costBuy*item.second)
            return false
        addItem(item)
        addGold(-item.first.costBuy*item.second)
        return true
    }

    fun setUser(user: User){
        this.user.email=user.email
        this.user.login=user.login
        this.user.uID=user.uID
        this.user.loggedIn=user.loggedIn
    }

    fun setTileTexture(texture:Bitmap){
        titleTexture= Bitmap.createBitmap(texture)
    }

    fun getTileTexture(): Bitmap =Bitmap.createBitmap(titleTexture)

    fun getAvatar():Bitmap= Bitmap.createBitmap(avatar)
}