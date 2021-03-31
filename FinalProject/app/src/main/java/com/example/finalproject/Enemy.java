package com.example.finalproject;

import java.util.HashMap;

public class Enemy {
    private String name;
    private int health, mana, damage, armor;
    private HashMap<Item, Integer> drop;

    public Enemy(String name, int health, int mana, int damage, int armor, HashMap<Item, Integer> drop) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.damage = damage;
        this.armor = armor;
        this.drop = drop;
    }

    public void attack (Player player){

    }

    public void take_damage(int damage){
        health-=damage;
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

    public HashMap<Item, Integer> getDrop() {
        return drop;
    }

    public void setDrop(HashMap<Item, Integer> drop) {
        this.drop = drop;
    }
}
