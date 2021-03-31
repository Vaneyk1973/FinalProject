package com.example.finalproject;


public class Armor extends Item {
    private int armor, weight, type_of_armor;

    public Armor(int armor, int weight, int type_of_armor, int rarity) {
        this.armor = armor;
        this.weight = weight;
        this.type_of_armor = type_of_armor;
        super.setRarity(rarity);
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getType_of_armor() {
        return type_of_armor;
    }

    public void setType_of_armor(int type_of_armor) {
        this.type_of_armor = type_of_armor;
    }
}

