package com.example.finalproject.service;

import com.example.finalproject.entities.Player;
import com.example.finalproject.fragments.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

public class Research {
    private final ArrayList<Research> requiredResearches;
    private final String name;
    private final int cost, tier, effect;
    private boolean researched, available;

    public Research(ArrayList<Research> requiredResearches, String name, int cost, int tier, int effect, boolean researched, boolean available) {
        this.requiredResearches = requiredResearches;
        this.name = name;
        this.cost = cost;
        this.tier = tier;
        this.researched = researched;
        this.available = available;
        this.effect = effect;
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

    private void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<Research> getRequiredResearches() {
        return requiredResearches;
    }

    public int getTier() {
        return tier;
    }

    public void enable() {
        if (requiredResearches != null)
            for (int i = 0; i < requiredResearches.size(); i++) {
                if (!MainActivity.researches.get(MainActivity.researches.indexOf(requiredResearches.get(i))).isResearched())
                    return;
            }
        available = true;
    }

    public int getEffect() {
        return effect;
    }

    public void affect(Player player) {
        for (int i = 0; i < 10; i++) {
            if (effect == i)
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
