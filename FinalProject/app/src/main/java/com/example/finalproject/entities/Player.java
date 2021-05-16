package com.example.finalproject.entities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.example.finalproject.fragments.MainActivity;
import com.example.finalproject.fragments.ResearchTreeFragment;
import com.example.finalproject.items.Armor;
import com.example.finalproject.items.Food;
import com.example.finalproject.items.Item;
import com.example.finalproject.items.Weapon;
import com.example.finalproject.service.Research;
import com.example.finalproject.service.spell.Spell;

import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity implements Parcelable {

    private int gold, research_points;
    private boolean logged_in;
    private boolean registered;
    private String user_login;
    private ArrayList<Integer> element_bonuses=new ArrayList<>();
    private ArrayList<Item> equipment=new ArrayList<>();
    private ArrayList<Item> inventory=new ArrayList<>();
    private ArrayList<Spell> spells=new ArrayList<>();
    private Pair<Integer, Integer> coordinates;
    private Spell chosen_spell;
    private Bitmap title_texture;
    private Enemy enemy;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(gold);
        dest.writeInt(research_points);
        dest.writeSerializable(element_bonuses);
        dest.writeInt(coordinates.first);
        dest.writeInt(coordinates.second);
        dest.writeSerializable(chosen_spell);
        dest.writeSerializable(equipment);
        dest.writeSerializable(inventory);
        dest.writeSerializable(spells);
        dest.writeParcelable(title_texture, flags);
        dest.writeParcelable(enemy, flags);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    protected Player(Parcel in) {
        super(in);
        gold = in.readInt();
        research_points = in.readInt();
        element_bonuses=new ArrayList<> ((ArrayList<Integer>) in.readSerializable());
        coordinates=new Pair<>(in.readInt(), in.readInt());
        chosen_spell=new Spell((Spell)in.readSerializable());
        equipment=new ArrayList<>((ArrayList)in.readSerializable());
        inventory=new ArrayList<>((ArrayList)in.readSerializable());
        spells=new ArrayList<>((ArrayList)in.readSerializable());
        title_texture = in.readParcelable(Bitmap.class.getClassLoader());
        enemy=new Enemy(in.readParcelable(Enemy.class.getClassLoader()));
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Player(int x, int y){
        setHealth_regen(1);
        setDamage(10);
        setLevel(1);
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
    }

    public void research(Research research){
        if (research.isAvailable()&&research.getCost()<=research_points){
            research.setResearched(true);
            research_points-=research.getCost();
            for (int i = 0; i< MainActivity.researches.size(); i++){
                if (MainActivity.researches.get(i).getTier()>research.getTier()&&
                        MainActivity.researches.get(i).getRequired_researches().contains(research)){
                    MainActivity.researches.get(i).enable();
                }
            }
            ResearchTreeFragment.research_hash_map.get(research).setBackgroundColor(Color.GREEN);
            research.affect(this);
        }
    }

    public void take_drop(){
        for (int i=0;i<getEnemy().getDrop().size();i++){
            if (new Random().nextInt(100)<=getEnemy().getDrop().get(i).second){
                getInventory().add(getEnemy().getDrop().get(i).first);
            }
        }
        setGold(getGold()+enemy.getGiven_gold());
        addExperience(enemy.getGiven_exp());
    }

    public void register(String login, String password){

    }

    public void cast_spell(){
        chosen_spell.affect(enemy);
    }

    public void choose_spell(Spell spell){
        chosen_spell=spell;
    }

    public void equip(Item item){
        if (item.getClass()== Weapon.class)
        {
            equipment.set(0, item);
            setDamage(getDamage()+((Weapon) item).getDamage());
        }
        else if (item.getClass()== Armor.class)
        {
            Armor item1=(Armor)item;
            switch (item1.getType_of_armor()){
                case 1:{
                    equipment.set(1, item1);
                    setArmor(getArmor()+ item1.getArmor());
                    break;
                }
                case 2:{
                    equipment.set(2, item1);
                    setArmor(getArmor()+ item1.getArmor());
                    break;
                }
                case 3:{
                    equipment.set(3, item1);
                    setArmor(getArmor()+ item1.getArmor());
                    break;
                }
                case 4:{
                    equipment.set(4, item1);
                    setArmor(getArmor()+ item1.getArmor());
                    break;
                }
                case 5:{
                    equipment.set(5, item1);
                    setArmor(getArmor()+ item1.getArmor());
                    break;
                }
            }
        }
    }

    public void eat(Food food){
        setHealth(getHealth()+ food.getHealth_recovery());
        setMana(getMana()+ food.getMana_recovery());
        if (getHealth()>getMax_health())
            setHealth(getMax_health());
        if (getMana()>getMax_mana())
            setMana(getMax_mana());
    }

    public void attack (){
        getEnemy().take_damage(getDamage());
    }

    public void level_up(){
        if (getExperience()>=getExperience_to_next_level_required()) {
            super.setLevel(this.getLevel()+1);
            setExperience(getExperience()-getExperience_to_next_level_required());
            setExperience_to_next_level_required(getLevel()*getLevel()*10);
            research_points+=3;
            setMana(getMana()+10);
            setHealth(getHealth()+10);
        }

    }

    public void addExperience(int exp){
        setExperience(exp+getExperience());
        level_up();
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

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Pair<Integer, Integer> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "gold=" + gold +
                ", experience="+getExperience()+
                ", research_points=" + research_points +
                ", element_bonuses=" + element_bonuses +
                ", coordinates=" + coordinates +
                ", chosen_spell=" + chosen_spell +
                ", equipment=" + equipment +
                ", inventory=" + inventory +
                ", spells=" + spells +
                ", title_texture=" + title_texture +
                ", enemy=" + enemy +
                '}';
    }

    public ArrayList<Integer> getElement_bonuses() {
        return element_bonuses;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
