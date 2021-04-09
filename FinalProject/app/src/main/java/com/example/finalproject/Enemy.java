package com.example.finalproject;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Enemy extends Entity {
    private ArrayList<Pair<Item, Integer>> drop;
    private boolean t=true;

    public Enemy(String name, int health, int mana, int damage, int armor, ArrayList drop) {
        setHealth_regen(5);
        setArmor(armor);
        setDamage(damage);
        setHealth(health);
        setName(name);
        setMax_health(health);
        setMax_mana(mana);
        setDrop(drop);
    }

    public Enemy (Enemy enemy){
        setHealth_regen(enemy.getHealth_regen());
        setMana_regen(enemy.getMana_regen());
        setArmor(enemy.getArmor());
        setDrop(enemy.getDrop());
        setDamage(enemy.getDamage());
        setExperience(enemy.getExperience());
        setExperience_to_next_level_required(enemy.getExperience_to_next_level_required());
        setHealth(enemy.getHealth());
        setLevel(enemy.getLevel());
        setMana(enemy.getMana());
        setMax_health(enemy.getMax_health());
        setMax_mana(enemy.getMax_mana());
        setName(enemy.getName());
        setPower_level(enemy.getPower_level());
    }

    public void attack (Player player){
        player.take_damage(super.getDamage());
    }

    public void fight(){
        if (t){
            attack(MainActivity.player);
            t=false;
        }
        else {
            defend();
            t=true;
        }
    }

    public void defend(){

    }

    public ArrayList<Pair<Item, Integer>> getDrop() {
        return drop;
    }

    public void setDrop(ArrayList<Pair<Item, Integer>> drop) {
        this.drop = drop;
    }
}
