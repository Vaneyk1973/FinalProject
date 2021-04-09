package com.example.finalproject;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity {
    private int gold, research_points;
    private Pair<Integer, Integer> coordinates;
    private Spell chosen_spell;
    private ArrayList<Integer> resistances=new ArrayList<>();
    private ArrayList<Item> equipment=new ArrayList<>();
    private ArrayList<Item> inventory=new ArrayList<>();
    private ArrayList<Spell> spells=new ArrayList<>(), fast_spells=new ArrayList<>();
    private Bitmap title_texture;
    private Enemy enemy;

    public Enemy getEnemy() {
        return enemy;
    }

    public Player(int x, int y){
        setHealth_regen(5);
        setDamage(10);
        setLevel(0);
        setExperience(0);
        setHealth(100);
        setMana(100);
        setMax_health(getHealth());
        setMax_mana(getMana());
        setPower_level(0);
        setExperience_to_next_level_required(10);
        setGold(0);
        setResearch_points(1);
        setCoordinates(new Pair<>(x, y));
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        resistances.add(null);
        resistances.add(null);
        resistances.add(null);
        resistances.add(null);
        resistances.add(null);
        resistances.add(null);
        resistances.add(null);
        resistances.add(null);
    }

    public void take_drop(){
        for (int i=0;i<getEnemy().getDrop().size();i++){
            if (new Random().nextInt(100)<=getEnemy().getDrop().get(i).second){
                getInventory().add(getEnemy().getDrop().get(i).first);
            }
        }
    }


    public void cast_spell(){
        chosen_spell.affect(enemy);
    }

    public void choose_spell(int spell){
        chosen_spell=fast_spells.get(spell);
    }

    public void setResistances(int resistance, int element){
        resistances.set(element, resistance);
    }

    public void equip(Item item){
        if (item.getClass()==Weapon.class)
        {
            equipment.set(0, item);
            setDamage(getDamage()+((Weapon) item).getDamage());
        }
        else if (item.getClass()==Helmet.class)
        {
            equipment.set(1, item);
            setArmor(getArmor()+((Helmet) item).getArmor());
        }
        else if (item.getClass()==Chestplate.class)
        {
            equipment.set(2, item);
            setArmor(getArmor()+((Chestplate) item).getArmor());
        }
        else if (item.getClass()==HandArmor.class)
        {
            equipment.set(3, item);
            setArmor(getArmor()+((HandArmor) item).getArmor());
        }
        else if (item.getClass()==LegArmor.class)
        {
            equipment.set(4, item);
            setArmor(getArmor()+((LegArmor) item).getArmor());
        }
        else if (item.getClass()==Boots.class)
        {
            equipment.set(5, item);
            setArmor(getArmor()+((Boots) item).getArmor());
        }
    }

    public void attack (){
        getEnemy().take_damage(getDamage());
    }

    public void level_up(){
        super.setLevel(this.getLevel());
        setExperience_to_next_level_required(getLevel()*getLevel()+10);
        research_points+=3;
        setMana(getMana()+10);
        setHealth(getHealth()+10);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public Bitmap getTitle_texture() {
        return title_texture;
    }

    public void setTitle_texture(Bitmap title_texture) {
        this.title_texture = title_texture;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<Spell> spells) {
        this.spells = spells;
    }

    public ArrayList<Spell> getFast_spells() {
        return fast_spells;
    }

    public void setFast_spells(ArrayList<Spell> fast_spells) {
        this.fast_spells = fast_spells;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getResearch_points() {
        return research_points;
    }

    public void setResearch_points(int research_points) {
        this.research_points = research_points;
    }

    public ArrayList<Item> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<Item> equipment) {
        this.equipment = equipment;
    }

    public ArrayList<Integer> getResistances() {
        return resistances;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Pair<Integer, Integer> coordinates) {
        this.coordinates = coordinates;
    }
}
