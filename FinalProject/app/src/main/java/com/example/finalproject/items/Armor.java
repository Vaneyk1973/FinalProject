package com.example.finalproject.items;


public class Armor extends Item {
    private final int armor, weight, typeOfArmor;

    public Armor(String name, int costSell, int costBuy, int armor, int weight, int typeOfArmor, int category, int rarity) {
        super(name, costSell, costBuy, rarity, category);
        this.armor = armor;
        this.weight = weight;
        this.typeOfArmor = typeOfArmor;
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

