package com.example.finalproject.service.spell

import android.os.Parcel
import android.os.Parcelable
import com.example.finalproject.entities.Enemy
import com.example.finalproject.fragments.MainActivity

class Spell() :Parcelable {
    var manaConsumption:Double=0.0
    var damage:Double=0.0
    var lastingTime:Double=0.0
    var name:String=""
    private val components:ArrayList<Component> = ArrayList()

    constructor(parcel: Parcel) : this() {
        name=parcel.readString().toString()
        manaConsumption=parcel.readDouble()
        damage=parcel.readDouble()
        lastingTime=parcel.readDouble()
        components.clear()
        parcel.readList(components, ArrayList::class.java.classLoader)
    }

    constructor(element: Element, type: Type, form: Form, manaChannel: ManaChannel, manaReservoir: ManaReservoir, name:String) : this() {
        components.add(Element(element))
        components.add(Type(type))
        components.add(Form(form))
        components.add(ManaChannel(manaChannel))
        components.add(ManaReservoir(manaReservoir))
        this.name=name
        manaConsumption=manaReservoir.volume
        damage=manaReservoir.volume*element.baseDamage
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(manaConsumption)
        parcel.writeDouble(damage)
        parcel.writeDouble(lastingTime)
        parcel.writeList(components)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Spell> {
        override fun createFromParcel(parcel: Parcel): Spell {
            return Spell(parcel)
        }

        override fun newArray(size: Int): Array<Spell?> {
            return arrayOfNulls(size)
        }
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

    fun getElement():Element= components[0] as Element
    fun getType():Type= components[1] as Type
}