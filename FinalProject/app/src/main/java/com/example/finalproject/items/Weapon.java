package com.example.finalproject.items;

public class Weapon extends Item {
    private int damage, typeOfDamage, typeOfWeapon;

    public Weapon(int damage, int typeOfDamage, int typeOfWeapon, String name) {
        super.setName(name);
        this.damage = damage;
        this.typeOfDamage = typeOfDamage;
        this.typeOfWeapon = typeOfWeapon;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getTypeOfDamage() {
        return typeOfDamage;
    }

    public void setTypeOfDamage(int typeOfDamage) {
        this.typeOfDamage = typeOfDamage;
    }

    public int getTypeOfWeapon() {
        return typeOfWeapon;
    }

    public void setTypeOfWeapon(int typeOfWeapon) {
        this.typeOfWeapon = typeOfWeapon;
    }
}
