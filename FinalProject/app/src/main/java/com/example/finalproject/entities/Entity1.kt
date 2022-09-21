package com.example.finalproject.entities

import android.graphics.Bitmap
import com.example.finalproject.items.Item
import com.google.firebase.database.DatabaseReference
import java.util.Calendar
import kotlin.math.max
import kotlin.random.Random

@Suppress("UNCHECKED_CAST")

open class Entity1(
    name: String,
    id: Int,
    override var health: Double,
    override var resistances: ArrayList<Double>,
    override val loot: Loot
) :
    Unit(name = name, id = id), Health, Drop {

    var texture: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        get() = Bitmap.createBitmap(field)
        set(texture) {
            field = Bitmap.createBitmap(texture)
        }

    constructor(
        name: String,
        id: Int,
        health: Double,
        resistances: ArrayList<Double>,
        loot: Loot,
        texture: Bitmap
    ) : this(name = name, id = id, health = health, resistances = resistances, loot = loot){
        this.texture=texture
    }

    override fun takeDamage(damage: Damage) {
        health = max(0.0, health - damage.realDamage(resistances))
    }

    override fun takeDamage(damage: Damage, ref: DatabaseReference) {
        ref.child("health").setValue(max(0.0, health - damage.realDamage(resistances)))
    }

    override fun dropLoot(): ArrayList<Pair<Int, Item>> {
        val droppedLoot: ArrayList<Pair<Int, Item>> = ArrayList()
        for (i in loot.loot) {
            val a = Random(Calendar.getInstance().timeInMillis).nextDouble()
            if (a < i.first)
                droppedLoot.add(Pair(i.second, i.third.clone()))
        }
        return droppedLoot.clone() as ArrayList<Pair<Int, Item>>
    }
}