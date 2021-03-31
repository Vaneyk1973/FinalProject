package com.example.finalproject;

public class Food extends Item {
    private int mana_recovery, health_recovery, weight;

    public Food(int mana_recovery, int health_recovery, int weight) {
        this.mana_recovery = mana_recovery;
        this.health_recovery = health_recovery;
        this.weight = weight;
    }

    public int getMana_recovery() {
        return mana_recovery;
    }

    public void setMana_recovery(int mana_recovery) {
        this.mana_recovery = mana_recovery;
    }

    public int getHealth_recovery() {
        return health_recovery;
    }

    public void setHealth_recovery(int health_recovery) {
        this.health_recovery = health_recovery;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
