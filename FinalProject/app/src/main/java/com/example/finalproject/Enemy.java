package com.example.finalproject;

import android.os.Parcelable;
import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Enemy extends Entity implements Parcelable {
    private ArrayList<Pair<Item, Integer>> drop;
    private int defence=0;
    private boolean t=true;

    public Enemy(String name, int health, int mana, int damage, int armor, int given_gold, int given_exp, ArrayList<Pair<Item, Integer>> drop) {
        setHealth_regen(2);
        setArmor(armor);
        setDamage(damage);
        setHealth(health);
        setName(name);
        setMax_health(health);
        setMax_mana(mana);
        setDrop(drop);
        setGiven_exp(given_exp);
        setGiven_gold(given_gold);
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
        setGiven_exp(enemy.getGiven_exp());
        setGiven_gold(enemy.getGiven_gold());
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

    public void be_affected_by_spell(Spell spell){
        defence+=getResistances().get(spell)*spell.getDamage();
    }
}
