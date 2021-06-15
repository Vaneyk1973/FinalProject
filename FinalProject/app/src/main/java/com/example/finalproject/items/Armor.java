package com.example.finalproject.items;


public class Armor extends Item {
    private final int armor, weight, typeOfArmor;

    public Armor(int cost, int armor, int weight, int type_of_armor, int rarity, String  name) {
        super.setCost(cost);
        this.armor = armor;
        this.weight = weight;
        this.typeOfArmor = type_of_armor;
        super.setRarity(rarity);
        super.setName(name);
    }

    public int getArmor() {
        return armor;
    }

    public int getWeight() {
        return weight;
    }

    public int getTypeOfArmor() {
        return typeOfArmor;
    }
}

