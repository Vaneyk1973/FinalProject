package com.example.finalproject.entities

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.example.finalproject.fragments.MainActivity
import com.example.finalproject.items.Item
import com.example.finalproject.service.Triplex

class Enemy() : Entity(), Parcelable {
    lateinit var drop:ArrayList<Triplex<Item, Int, Int>>
    lateinit var texture: Bitmap
    var defence:Int=0
    var duel:Boolean=false
    var fightTick:Int=0

    constructor(health: Double, mana:Double, damage:Double, armor:Double, givenGold:Int, givenExp:Int,
                name:String,
                drop:ArrayList<Triplex<Item, Int, Int>>,
                texture: Bitmap): this() {
        healthRegen=2.0
        this.armor=armor
        this.health=health
        this.mana=mana
        this.damage=damage
        this.givenGold=givenGold
        this.givenExp=givenExp
        this.name=name
        this.drop=ArrayList(drop)
        this.texture= Bitmap.createBitmap(texture)
    }

    constructor(enemy: Enemy):this(){

    }

    constructor(parcel: Parcel) : this() {
        texture = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        defence = parcel.readInt()
        duel = parcel.readByte() != 0.toByte()
        fightTick = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeParcelable(texture, flags)
        parcel.writeInt(defence)
        parcel.writeByte(if (duel) 1 else 0)
        parcel.writeInt(fightTick)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Enemy> {
        override fun createFromParcel(parcel: Parcel): Enemy {
            return Enemy(parcel)
        }

        override fun newArray(size: Int): Array<Enemy?> {
            return arrayOfNulls(size)
        }
    }


    fun attack(){
        MainActivity.player.takeDamage(damage)
    }

    fun defend(){

    }

    fun fight(){
        if (fightTick%2==0)
            attack()
        else defend()
        fightTick++
    }
}