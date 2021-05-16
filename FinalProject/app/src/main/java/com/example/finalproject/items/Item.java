package com.example.finalproject.items;

import java.io.Serializable;

public class Item implements Serializable {
    private int cost, rarity, category;
    private String name;

    public Item(){}

    public Item(String name){
        this.name=name;
    }

    public Item(int cost, String name) {
        this.cost = cost;
        this.name = name;
    }

    public Item(int cost, int rarity, String name) {
        this.cost = cost;
        this.rarity = rarity;
        this.name = name;
    }

    public Item(int cost, int rarity, int category, String name) {
        this.cost = cost;
        this.rarity = rarity;
        this.category = category;
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}

