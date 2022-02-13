package com.example.finalproject.entities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.finalproject.service.spell.Spell;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

public class Entity implements Parcelable {
    private int level, experience, powerLevel, experienceToNextLevelRequired,
            damage, armor, givenExp, givenGold;
    private double health, max_health, mana, max_mana, health_regen, mana_regen;
    private String name;
    private HashMap<Spell, Double> resistances = new HashMap<>();

    public Entity() {}

    protected Entity(Parcel in) {
        level = in.readInt();
        experience = in.readInt();
        powerLevel = in.readInt();
        experienceToNextLevelRequired = in.readInt();
        damage = in.readInt();
        armor = in.readInt();
        givenExp = in.readInt();
        givenGold = in.readInt();
        health = in.readDouble();
        max_health = in.readDouble();
        mana = in.readDouble();
        max_mana = in.readDouble();
        health_regen = in.readDouble();
        mana_regen = in.readDouble();
        name = in.readString();
        resistances = in.readHashMap(new GenericTypeIndicator<HashMap<Spell, Double>>(){}.getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(experience);
        dest.writeInt(powerLevel);
        dest.writeInt(experienceToNextLevelRequired);
        dest.writeInt(damage);
        dest.writeInt(armor);
        dest.writeInt(givenExp);
        dest.writeInt(givenGold);
        dest.writeDouble(health);
        dest.writeDouble(max_health);
        dest.writeDouble(mana);
        dest.writeDouble(max_mana);
        dest.writeDouble(health_regen);
        dest.writeDouble(mana_regen);
        dest.writeString(name);
        dest.writeSerializable(resistances);
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

    public void take_damage(int damage) {
       damage= Math.max(damage - armor, 0);
       health-=damage;
    }

    public void regenerate() {
        setHealth(getHealth() + getHealthRegen());
        setMana(getMana() + getManaRegen());
        if (getMana() > getMaxMana())
            setMana(getMaxMana());
        if (getHealth() > getMaxHealth())
            setHealth(getMaxHealth());
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxHealth() {
        return max_health;
    }

    public void setMaxHealth(double max_health) {
        this.max_health = max_health;
    }

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public double getMaxMana() {
        return max_mana;
    }

    public void setMaxMana(double max_mana) {
        this.max_mana = max_mana;
    }

    public double getHealthRegen() {
        return health_regen;
    }

    public void setHealthRegen(double health_regen) {
        this.health_regen = health_regen;
    }

    public double getManaRegen() {
        return mana_regen;
    }

    public void setManaRegen(double mana_regen) {
        this.mana_regen = mana_regen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public int getExperienceToNextLevelRequired() {
        return experienceToNextLevelRequired;
    }

    public void setExperienceToNextLevelRequired(int experienceToNextLevelRequired) {
        this.experienceToNextLevelRequired = experienceToNextLevelRequired;
    }

    public HashMap<Spell, Double> getResistances() {
        return resistances;
    }

    public void setResistances(HashMap<Spell, Double> resistances) {
        this.resistances = new HashMap<>(resistances);
    }

    public int getGivenExp() {
        return givenExp;
    }

    public void setGivenExp(int givenExp) {
        this.givenExp = givenExp;
    }

    public int getGivenGold() {
        return givenGold;
    }

    public void setGivenGold(int givenGold) {
        this.givenGold = givenGold;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
