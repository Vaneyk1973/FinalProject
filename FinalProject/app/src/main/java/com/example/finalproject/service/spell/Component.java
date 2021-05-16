package com.example.finalproject.service.spell;

public class Component {
    private String name;
    private boolean available;

    public Component(String name, boolean available) {
        this.name = name;
        this.available=available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable() {
        available=true;
    }
}

