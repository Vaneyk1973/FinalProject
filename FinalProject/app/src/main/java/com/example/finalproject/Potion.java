package com.example.finalproject;

public class Potion extends Item {
    public int lasting_time, strength, effect;

    public Potion(int effect, String name) {
        this.effect = effect;
        super.setName(name);
    }

    public Potion(int lasting_time, int strength, int effect) {
        this.lasting_time = lasting_time;
        this.strength = strength;
        this.effect = effect;
    }

    public int getLasting_time() {
        return lasting_time;
    }

    public void setLasting_time(int lasting_time) {
        this.lasting_time = lasting_time;
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
