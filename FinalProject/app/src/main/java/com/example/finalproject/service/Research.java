package com.example.finalproject.service;

import com.example.finalproject.entities.Player;
import com.example.finalproject.fragments.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

public class Research {
    private final ArrayList<Research> required_researches;
    private final String name;
    private final int cost, tier, effect;
    private boolean researched, available;

    public Research(ArrayList<Research> required_researches, String name, int cost, int tier, int effect, boolean researched, boolean available) {
        this.required_researches = required_researches;
        this.name = name;
        this.cost = cost;
        this.tier = tier;
        this.researched = researched;
        this.available = available;
        this.effect=effect;
    }

    public boolean isResearched() {
        return researched;
    }

    public void setResearched(boolean researched) {
        this.researched = researched;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<Research> getRequired_researches() {
        return required_researches;
    }

    public int getTier() {
        return tier;
    }

    public void enable() {
        for (int i=0;i<required_researches.size();i++){
            if (!required_researches.get(i).isResearched())
                return;
        }
        available=true;
    }

    public int getEffect() {
        return effect;
    }

    public void affect(Player player){
        for (int i=0;i<10;i++){
            if (effect==i)
                MainActivity.elements.get(i).setAvailable();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Research research = (Research) o;
        return Objects.equals(name, research.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
