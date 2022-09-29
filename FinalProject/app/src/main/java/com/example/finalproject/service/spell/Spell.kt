package com.example.finalproject.service.spell

import android.os.Parcel
import android.os.Parcelable
import com.example.finalproject.entities.Damage
import com.example.finalproject.entities.Enemy
import com.example.finalproject.fragments.MainActivity

class Spell() {
    //TODO("Implement new damage")
    var manaConsumption:Double=0.0
    var damage: Damage =Damage(ArrayList())
    var lastingTime:Double=0.0
    var name:String=""
    private val components:ArrayList<Component> = ArrayList()

    constructor(element: Element, type: Type, form: Form, manaChannel: ManaChannel, manaReservoir: ManaReservoir, name:String) : this() {
        components.add(Element(element))
        components.add(Type(type))
        components.add(Form(form))
        components.add(ManaChannel(manaChannel))
        components.add(ManaReservoir(manaReservoir))
        this.name=name
        manaConsumption=manaReservoir.volume
        //damage=manaReservoir.volume*element.baseDamage
        lastingTime=manaReservoir.volume/manaChannel.mps
    }

    constructor(spell: Spell):this(){
        components.clear()
        components.addAll(spell.components)
        damage=spell.damage
        lastingTime=spell.lastingTime
        manaConsumption=spell.manaConsumption
        name=spell.name
    }

    private fun consumeMana(){
        MainActivity.player.mana-=manaConsumption
    }

    fun affect(enemy: Enemy){
        if (MainActivity.player.mana>=manaConsumption){
            consumeMana()
            enemy.takeDamage(damage)
        }
    }

    fun getElement():Element= Element(components[0] as Element)
    fun getType():Type= Type(components[1] as Type)
}