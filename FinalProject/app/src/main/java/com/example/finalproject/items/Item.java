package com.example.finalproject.items;

import org.xmlpull.v1.XmlPullParser;

import java.io.Serializable;

public class Item implements Serializable {
    private int costSell, costBuy, rarity, category;
    private String name;

    public Item(){}

    public Item(String name, int costSell, int costBuy, int rarity, int category) {
        this.costSell = costSell;
        this.costBuy = costBuy;
        this.rarity = rarity;
        this.category = category;
        this.name = name;
    }

    public int getCostSell() {
        return costSell;
    }

    public void setCostSell(int costSell) {
        this.costSell = costSell;
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

    public int getCostBuy() {
        return costBuy;
    }

    public void setCostBuy(int costBuy) {
        this.costBuy = costBuy;
    }
}

