package com.example.finalproject;

import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Console;
import java.util.AbstractMap;
import java.util.TreeSet;

public class Spell {
    private int form_of_spell, name, mana_consumption, element, damage;

    public Spell(int form_of_spell, int mana_consumption, int element, int damage) {
        this.form_of_spell = form_of_spell;
        this.mana_consumption = mana_consumption;
        this.element = element;
        this.damage = damage;
    }

    public void affect (Enemy enemy){
        if (MainActivity.player.getMana()>=mana_consumption){
            consume_mana();
            enemy.take_damage(damage);
        }
        else {
            Log.d("", "Not enough mana");
        }
    }

    public void consume_mana(){
        MainActivity.player.setMana(MainActivity.player.getMana()-mana_consumption);
    }
}

