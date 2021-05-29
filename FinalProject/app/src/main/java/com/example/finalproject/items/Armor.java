package com.example.finalproject.items;


public class Armor extends Item {
    private final int armor, weight, type_of_armor;

    public Armor(int cost, int armor, int weight, int type_of_armor, int rarity, String  name) {
        super.setCost(cost);
        this.armor = armor;
        this.weight = weight;
        this.type_of_armor = type_of_armor;
        super.setRarity(rarity);
        super.setName(name);
    }

    public int getArmor() {
        return armor;
    }

    public int getWeight() {
        return weight;
    }

    public int getType_of_armor() {
        return type_of_armor;
    }
}

