package com.example.finalproject.entities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;

import com.example.finalproject.fragments.MainActivity;
import com.example.finalproject.fragments.ResearchTreeFragment;
import com.example.finalproject.items.Armor;
import com.example.finalproject.items.Food;
import com.example.finalproject.items.Item;
import com.example.finalproject.items.Weapon;
import com.example.finalproject.service.A;
import com.example.finalproject.service.Research;
import com.example.finalproject.service.User;
import com.example.finalproject.service.spell.Spell;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity implements Parcelable {

    private int gold, research_points;
    private boolean registered, chat_mode;
    private User user;
    private ArrayList<Integer> element_bonuses = new ArrayList<>();
    private ArrayList<Item> equipment = new ArrayList<>();
    private ArrayList<Pair<Item, Integer>> inventory = new ArrayList<>();
    private ArrayList<Spell> spells = new ArrayList<>();
    private Pair<Integer, Integer> coordinates;
    private Spell chosen_spell;
    private Bitmap title_texture;
    private Bitmap avatar;
    private Enemy enemy;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(gold);
        dest.writeInt(research_points);
        dest.writeString(registered + "");
        dest.writeString(chat_mode + "");
        dest.writeSerializable(element_bonuses);
        dest.writeInt(coordinates.first);
        dest.writeInt(coordinates.second);
        dest.writeSerializable(chosen_spell);
        dest.writeSerializable(equipment);
        dest.writeParcelable((Parcelable) inventory, flags);
        dest.writeSerializable(spells);
        dest.writeParcelable(title_texture, flags);
        dest.writeParcelable(enemy, flags);
        dest.writeString(new Gson().toJson(user));
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
        registered = new Boolean(in.readString());
        chat_mode = new Boolean(in.readString());
        element_bonuses = new ArrayList<>((ArrayList<Integer>) in.readSerializable());
        coordinates = new Pair<>(in.readInt(), in.readInt());
        chosen_spell = new Spell((Spell) in.readSerializable());
        equipment = new ArrayList<>((ArrayList) in.readSerializable());
        inventory = new ArrayList<>((ArrayList) in.readParcelable(ArrayList.class.getClassLoader()));
        spells = new ArrayList<>((ArrayList) in.readSerializable());
        title_texture = in.readParcelable(Bitmap.class.getClassLoader());
        enemy = new Enemy(in.readParcelable(Enemy.class.getClassLoader()));
        user = new Gson().fromJson(in.readString(), User.class);
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Player(int x, int y) {
        setHealth_regen(3);
        setMana_regen(0.3);
        setDamage(10);
        setLevel(1);
        setExperience(0);
        setHealth(50);
        setMana(10);
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
        user = new User("", "");
        user.log_out();
        chat_mode = true;
        registered = false;
    }

    public void research(Research research) {
        if (research.isAvailable() && !research.isResearched() && research.getCost() <= research_points) {
            research.setResearched(true);
            research_points -= research.getCost();
            for (int i = 0; i < MainActivity.researches.size(); i++) {
                MainActivity.researches.get(i).enable();
            }
            ResearchTreeFragment.research_hash_map.get(research).setBackgroundColor(Color.GREEN);
            research.affect(this);
        }
    }

    public void take_drop() {
        for (int i = 0; i < getEnemy().getDrop().size(); i++) {
            if (new Random().nextInt(100) <= getEnemy().getDrop().get(i).second) {
                Item drop = getEnemy().getDrop().get(i).first;
                if (contains(inventory, new Pair(drop, new Object()))) {
                    int a = get(inventory, drop);
                    inventory.set(a, new Pair<>(drop, inventory.get(a).second + 1));
                } else {
                    inventory.add(new Pair<>(drop, 1));
                }
            }
        }
        setGold(getGold() + enemy.getGiven_gold());
        addExperience(enemy.getGiven_exp());
    }

    public void cast_spell() {
        chosen_spell.affect(enemy);
        Log.d("KKKGGGG", chosen_spell.getMana_consumption()+" "+getMana()+" "+getMana_regen());
    }

    public void choose_spell(Spell spell) {
        chosen_spell = spell;
    }

    public void equip(Item item) {
        if (item.getClass() == Weapon.class) {
            equipment.set(0, item);
            setDamage(getDamage() + ((Weapon) item).getDamage());
        } else if (item.getClass() == Armor.class) {
            Armor item1 = (Armor) item;
            switch (item1.getType_of_armor()) {
                case 1: {
                    if (equipment.get(((Armor) item).getType_of_armor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(1)).getArmor());
                    equipment.set(1, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 2: {
                    if (equipment.get(((Armor) item).getType_of_armor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(2)).getArmor());
                    equipment.set(2, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 3: {
                    if (equipment.get(((Armor) item).getType_of_armor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(3)).getArmor());
                    equipment.set(3, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 4: {
                    if (equipment.get(((Armor) item).getType_of_armor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(4)).getArmor());
                    equipment.set(4, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 5: {
                    if (equipment.get(((Armor) item).getType_of_armor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(5)).getArmor());
                    equipment.set(5, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
            }
        }
    }

    public void eat(Food food) {
        setHealth(getHealth() + food.getHealth_recovery());
        setMana(getMana() + food.getMana_recovery());
        if (getHealth() > getMax_health())
            setHealth(getMax_health());
        if (getMana() > getMax_mana())
            setMana(getMax_mana());
    }

    public void attack() {
        getEnemy().take_damage(getDamage());
    }

    public void level_up() {
        while (getExperience() >= getExperience_to_next_level_required()) {
            super.setLevel(this.getLevel() + 1);
            setExperience(getExperience() - getExperience_to_next_level_required());
            setExperience_to_next_level_required(getLevel() * getLevel() * 10);
            research_points += 3;
            setMax_mana(getMana() * 1.6);
            setMana(getMax_mana());
            setMax_health(getMax_health() * 1.6);
            setHealth(getMax_health());
            setMana_regen(getMana_regen() * 1.7);
            setHealth_regen(getHealth_regen() * 1.7);
        }
    }

    public void addExperience(int exp) {
        setExperience(exp + getExperience());
        level_up();
    }

    public ArrayList<Pair<Item, Integer>> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Pair<Item, Integer>> inventory) {
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
                ", experience=" + getExperience() +
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean getChat_mode() {
        return chat_mode;
    }

    public void setChat_mode(boolean chat_mode) {
        this.chat_mode = chat_mode;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    private boolean contains(ArrayList<Pair<Item, Integer>> data, Pair<Item, Integer> element) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).first != null) {
                if (data.get(i).first.getName().equals(element.first.getName()))
                    return true;
            }
        }
        return false;
    }

    private int get(ArrayList<Pair<Item, Integer>> data, Item element) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).first.getName().equals(element.getName()))
                return i;
        }
        return -1;
    }
}
