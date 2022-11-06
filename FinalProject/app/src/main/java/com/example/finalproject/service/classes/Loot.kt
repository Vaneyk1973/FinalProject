package com.example.finalproject.service.classes

import com.example.finalproject.MainActivity
import com.example.finalproject.service.classes.items.Item
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.random.Random

/**
 *  The items which could be given to the [Player] when killing an [Entity]
 *  @property loot a list of possible items in form of <the percent chance of dropping in range [0, 100] :[Int], the quantity of items :[Int], the given item :[Item]>
 */
@Serializable
data class Loot(val loot: ArrayList<Triple<Int, Int, Int>> = ArrayList(), var gold:Int=0, var exp:Int=0) {

    /**
     * @return the items according to their chances
     */
    fun dropLoot(): ArrayList<Pair<Int, Item>> {
        val random = Random(Calendar.getInstance().timeInMillis)
        val droppedLoot: ArrayList<Pair<Int, Item>> = ArrayList()
        for (item in loot) {
            if (random.nextInt(100) < item.first) {
                droppedLoot.add(Pair(item.second, MainActivity.assets.items[item.third]!!))
            }
        }
        return droppedLoot
    }

}