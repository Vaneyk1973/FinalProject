package com.example.finalproject.items;

public class Weapon extends Item {
    private int damage, type_of_damage, type_of_weapon;

    public Weapon(int damage, int type_of_damage, int type_of_weapon, String name) {
        super.setName(name);
        this.damage = damage;
        this.type_of_damage = type_of_damage;
        this.type_of_weapon = type_of_weapon;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getType_of_damage() {
        return type_of_damage;
    }

    public void setType_of_damage(int type_of_damage) {
        this.type_of_damage = type_of_damage;
    }

    public int getType_of_weapon() {
        return type_of_weapon;
    }

    public void setType_of_weapon(int type_of_weapon) {
        this.type_of_weapon = type_of_weapon;
    }
}
