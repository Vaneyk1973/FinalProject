package com.example.finalproject;

import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;

public class Player {
    private int level, experience, gold, health, mana, power_level, experience_to_next_level_required, research_points, damage, armor;
    private Pair<Integer, Integer> coordinates;
    private Spell chosen_spell;
    private ArrayList<Integer> resistances=new ArrayList<>();
    private ArrayList<Item> equipment=new ArrayList<>();
    private ArrayList<Item> inventory=new ArrayList<>();
    private ArrayList<Spell> spells=new ArrayList<>(), fast_spells=new ArrayList<>();
    private Enemy enemy;

    public Player(int x, int y){
        level=0;
        experience=0;
        gold=0;
        health=100;
        mana=100;
        power_level=0;
        experience_to_next_level_required=10;
        research_points=1;
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
        this.coordinates=new Pair<>(x, y);
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
            damage+=((Weapon) item).getDamage();
        }
        else if (item.getClass()==Helmet.class)
        {
            equipment.set(1, item);
            armor+=((Helmet) item).getArmor();
        }
        else if (item.getClass()==Chestplate.class)
        {
            equipment.set(2, item);
            armor+=((Chestplate) item).getArmor();
        }
        else if (item.getClass()==HandArmor.class)
        {
            equipment.set(3, item);
            armor+=((HandArmor) item).getArmor();
        }
        else if (item.getClass()==LegArmor.class)
        {
            equipment.set(4, item);
            armor+=((LegArmor) item).getArmor();
        }
        else if (item.getClass()==Boots.class)
        {
            equipment.set(5, item);
            armor+=((Boots) item).getArmor();
        }
    }

    public void attack (){
        enemy.take_damage(damage);
    }

    public void take_damage(int damage){
        health-=damage;
    }

    public void level_up(){
        level++;
        experience_to_next_level_required=(level+1)*(level+1)*level*level+10;
        research_points+=3;
        mana+=10;
        health+=10;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getPower_level() {
        return power_level;
    }

    public void setPower_level(int power_level) {
        this.power_level = power_level;
    }

    public int getExperience_to_next_level_required() {
        return experience_to_next_level_required;
    }

    public void setExperience_to_next_level_required(int experience_to_next_level_required) {
        this.experience_to_next_level_required = experience_to_next_level_required;
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
