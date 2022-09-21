
package com.example.finalproject.entities
/*
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.example.finalproject.service.spell.Spell
import kotlin.math.max
import kotlin.math.min

open class Entity():Parcelable{
    var level:Int=0
    var experience:Double=0.0
    var powerLevel:Int=0
    var experienceToNextLevelRequired:Double=0.0
    var damage:Double=0.0
    var armor:Double=0.0
    var givenExp:Double=0.0
    var givenGold:Double=0.0
    var health:Double=0.0
    var maxHealth:Double=0.0
    var mana:Double=0.0
    var maxMana:Double=0.0
    var healthRegen:Double=0.0
    var manaRegen:Double=0.0
    var name:String=""
    private lateinit var resistances:HashMap<Spell, Double>


    constructor(level:Int,
                experience:Double,
                powerLevel:Int,
                experienceToNextLevelRequired:Double,
                damage:Double,
                armor:Double,
                givenExp:Double,
                givenGold:Double,
                health:Double,
                maxHealth:Double,
                mana:Double,
                maxMana:Double,
                healthRegen:Double,
                manaRegen:Double,
                name:String,
                resistances:HashMap<Spell, Double>) : this(){
        this.level=level
        this.experience=experience
        this.powerLevel=powerLevel
        this.experienceToNextLevelRequired=experienceToNextLevelRequired
        this.maxHealth=maxHealth
        this.maxMana=maxMana
        this.manaRegen=manaRegen
        this.resistances=HashMap(resistances)
        this.healthRegen=healthRegen
        this.armor=armor
        this.health=health
        this.mana=mana
        this.maxMana=mana
        this.maxHealth=health
        this.damage=damage
        this.givenGold=givenGold
        this.givenExp=givenExp
        this.name=name
                }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readBundle()?.getSerializable("resistances") as HashMap<Spell, Double>
    )

    fun takeDamage(damage: Double){
        health-=max(damage-armor, 0.0)
    }

    fun regenerate(){
        health=min(maxHealth, health+healthRegen)
        mana=min(maxMana, mana+maxMana)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(level)
        parcel.writeDouble(experience)
        parcel.writeInt(powerLevel)
        parcel.writeDouble(experienceToNextLevelRequired)
        parcel.writeDouble(damage)
        parcel.writeDouble(armor)
        parcel.writeDouble(givenExp)
        parcel.writeDouble(givenGold)
        parcel.writeDouble(health)
        parcel.writeDouble(maxHealth)
        parcel.writeDouble(mana)
        parcel.writeDouble(maxMana)
        parcel.writeDouble(healthRegen)
        parcel.writeDouble(manaRegen)
        parcel.writeString(name)
        val b=Bundle()
        b.putSerializable("resistances", resistances)
        parcel.writeBundle(b)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entity> {
        override fun createFromParcel(parcel: Parcel): Entity {
            return Entity(parcel)
        }

        override fun newArray(size: Int): Array<Entity?> {
            return arrayOfNulls(size)
        }
    }
}
*/
