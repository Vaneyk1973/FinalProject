package com.example.finalproject.service

import com.example.finalproject.service.classes.Recipe
import com.example.finalproject.service.classes.Research
import com.example.finalproject.service.classes.Task
import com.example.finalproject.service.classes.entities.Enemy
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.spell.*
import kotlinx.serialization.Serializable

@Serializable
object Statistics {
    val chancesOfFight =
        HashMap<Int, Int>() //<id of location (field, forest etc.), chance of getting into a fight>
    val chancesOfEnemy =
        HashMap<Int, ArrayList<Pair<Int, Int>>>() //<id of location, <chance of getting into a fight with that enemy, id of an enemy>
    val enemies = HashMap<Int, Enemy>() //<id of an enemy, object template>
    val elements = ArrayList<Element>()
    val manaChannels = ArrayList<ManaChannel>()
    val types = ArrayList<Type>()
    val forms = ArrayList<Form>()
    val manaReservoirs = ArrayList<ManaReservoir>()
    val researches = ArrayList<Research>()
    val recipes = ArrayList<Recipe>()
    val items = HashMap<Int, Item>() //<id of an item, object template>
    val shopList = ArrayList<Item>() //<items available in the shop>
    val tasks = ArrayList<Task>()
    val categories = HashMap<Int, String>()
    val ids = HashMap<Int, Int>()
    val names = HashMap<Int, String>()
}