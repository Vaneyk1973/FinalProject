package com.example.finalproject.items;

public class Potion extends Item {
    public int lastingTime, strength, effect;

    public Potion(int effect, String name) {
        this.effect = effect;
        super.setName(name);
    }

    public Potion(int lastingTime, int strength, int effect) {
        this.lastingTime = lastingTime;
        this.strength = strength;
        this.effect = effect;
    }

    public int getLastingTime() {
        return lastingTime;
    }

    public void setLastingTime(int lastingTime) {
        this.lastingTime = lastingTime;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }
}
