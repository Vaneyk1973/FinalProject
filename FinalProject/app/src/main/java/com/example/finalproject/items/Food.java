package com.example.finalproject.items;

public class Food extends Item {
    private int manaRecovery, healthRecovery, weight;

    public Food(int manaRecovery, int healthRecovery, int weight) {
        this.manaRecovery = manaRecovery;
        this.healthRecovery = healthRecovery;
        this.weight = weight;
    }

    public int getManaRecovery() {
        return manaRecovery;
    }

    public void setManaRecovery(int manaRecovery) {
        this.manaRecovery = manaRecovery;
    }

    public int getHealthRecovery() {
        return healthRecovery;
    }

    public void setHealthRecovery(int healthRecovery) {
        this.healthRecovery = healthRecovery;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
