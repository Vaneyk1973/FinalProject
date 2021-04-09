package com.example.finalproject;

import java.util.HashMap;

public class Entity {
    private int level, experience, health, max_health, mana, max_mana, power_level, experience_to_next_level_required,
            damage, armor, health_regen, mana_regen;
    private String name;

    public void take_damage(int damage){
        health-=damage;
    }


    public void regenerate(){
        setHealth(getHealth()+getHealth_regen());
        setMana(getMana()+getMana_regen());
        if (getMana()>getMax_mana())
            setMana(getMax_mana());
        if (getHealth()>getMax_health())
            setHealth(getMax_health());
    }

    public int getHealth_regen() {
        return health_regen;
    }

    public void setHealth_regen(int health_regen) {
        this.health_regen = health_regen;
    }

    public int getMana_regen() {
        return mana_regen;
    }

    public void setMana_regen(int mana_regen) {
        this.mana_regen = mana_regen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
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

    public int getMax_health() {
        return max_health;
    }

    public void setMax_health(int max_health) {
        this.max_health = max_health;
    }

    public int getMax_mana() {
        return max_mana;
    }

    public void setMax_mana(int max_mana) {
        this.max_mana = max_mana;
    }

    public int getPower_level() {
        return power_level;
    }

    public void setPower_level(int power_level) {
        this.power_level = power_level;
    }

    public int getExperience_to_next_level_required() {
        return experience_to_next_level_required;
    }

    public void setExperience_to_next_level_required(int experience_to_next_level_required) {
        this.experience_to_next_level_required = experience_to_next_level_required;
    }
}
