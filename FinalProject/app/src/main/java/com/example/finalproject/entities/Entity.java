package com.example.finalproject.entities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.finalproject.service.spell.Spell;

import java.util.HashMap;

public class Entity implements Parcelable {
    private int level, experience, powerLevel, experienceToNextLevelRequired,
            damage, armor, givenExp, givenGold;
    private double health, max_health, mana, max_mana, health_regen, mana_regen;
    private String name;
    private HashMap<Spell, Double> resistances = new HashMap<>();

    public Entity() {
    }

    protected Entity(Parcel in) {
        level = in.readInt();
        experience = in.readInt();
        health = in.readDouble();
        max_health = in.readDouble();
        mana = in.readDouble();
        max_mana = in.readDouble();
        powerLevel = in.readInt();
        experienceToNextLevelRequired = in.readInt();
        damage = in.readInt();
        armor = in.readInt();
        health_regen = in.readDouble();
        mana_regen = in.readDouble();
        givenExp = in.readInt();
        givenGold = in.readInt();
        name = in.readString();
        resistances = new HashMap<>((HashMap<Spell, Double>) in.readBundle().getSerializable("resistances"));
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
       damage=damage-armor<=0?0:damage-armor;
       health-=damage;
    }


    public void regenerate() {
        setHealth(getHealth() + getHealth_regen());
        setMana(getMana() + getMana_regen());
        if (getMana() > getMax_mana())
            setMana(getMax_mana());
        if (getHealth() > getMax_health())
            setHealth(getMax_health());
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMax_health() {
        return max_health;
    }

    public void setMax_health(double max_health) {
        this.max_health = max_health;
    }

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public double getMax_mana() {
        return max_mana;
    }

    public void setMax_mana(double max_mana) {
        this.max_mana = max_mana;
    }

    public double getHealth_regen() {
        return health_regen;
    }

    public void setHealth_regen(double health_regen) {
        this.health_regen = health_regen;
    }

    public double getMana_regen() {
        return mana_regen;
    }

    public void setMana_regen(double mana_regen) {
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
        this.resistances = resistances;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(experience);
        dest.writeDouble(health);
        dest.writeDouble(max_health);
        dest.writeDouble(mana);
        dest.writeDouble(max_mana);
        dest.writeInt(powerLevel);
        dest.writeInt(experienceToNextLevelRequired);
        dest.writeInt(damage);
        dest.writeInt(armor);
        dest.writeDouble(health_regen);
        dest.writeDouble(mana_regen);
        dest.writeInt(givenExp);
        dest.writeInt(givenGold);
        dest.writeString(name);
        Bundle b = new Bundle();
        b.putSerializable("resistances", resistances);
        dest.writeBundle(new Bundle(b));
    }
}
