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
import com.example.finalproject.items.Recipe;
import com.example.finalproject.items.Weapon;
import com.example.finalproject.service.Research;
import com.example.finalproject.service.User;
import com.example.finalproject.service.spell.Spell;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity implements Parcelable {

    private int gold, researchPoints, mapNum, duelNum, duelPnum;
    private boolean chatMode;
    private User user;
    private ArrayList<Integer> elementBonuses = new ArrayList<>();
    private ArrayList<Item> equipment = new ArrayList<>();
    private ArrayList<Pair<Item, Integer>> inventory = new ArrayList<>();
    private ArrayList<Spell> spells = new ArrayList<>();
    private ArrayList<Pair<Integer, Integer>> coordinates=new ArrayList<>();
    private Spell chosenSpell;
    private Bitmap titleTexture;
    private Bitmap avatar;
    private Enemy enemy;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(gold);
        dest.writeInt(researchPoints);
        dest.writeString(chatMode + "");
        dest.writeSerializable(elementBonuses);
        dest.writeSerializable(coordinates);
        dest.writeSerializable(chosenSpell);
        dest.writeSerializable(equipment);
        dest.writeParcelable((Parcelable) inventory, flags);
        dest.writeSerializable(spells);
        dest.writeParcelable(titleTexture, flags);
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
        researchPoints = in.readInt();
        chatMode = new Boolean(in.readString());
        elementBonuses = new ArrayList<>((ArrayList<Integer>) in.readSerializable());
        coordinates =new ArrayList<>((ArrayList<Pair<Integer, Integer>>)in.readSerializable());
        chosenSpell = new Spell((Spell) in.readSerializable());
        equipment = new ArrayList<>((ArrayList) in.readSerializable());
        inventory = new ArrayList<>((ArrayList) in.readParcelable(ArrayList.class.getClassLoader()));
        spells = new ArrayList<>((ArrayList) in.readSerializable());
        titleTexture = in.readParcelable(Bitmap.class.getClassLoader());
        enemy = new Enemy(in.readParcelable(Enemy.class.getClassLoader()));
        user = new Gson().fromJson(in.readString(), User.class);
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Player(int x, int y) {
        setHealthRegen(3);
        setManaRegen(0.3);
        setDamage(10);
        setLevel(1);
        setExperience(0);
        setHealth(50);
        setMana(10);
        setMaxHealth(getHealth());
        setMaxMana(getMana());
        setPowerLevel(0);
        setExperienceToNextLevelRequired((int) Math.ceil(expFormula(1.0)));
        setGold(0);
        setResearchPoints(1);
        coordinates.add(new Pair<>(x, y));
        coordinates.add(new Pair<>(6, 3));
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        equipment.add(null);
        user = new User("", "");
        chatMode = false;
    }

    public void research(Research research) {
        if (research.isAvailable() && !research.isResearched() && research.getCost() <= researchPoints) {
            research.setResearched(true);
            researchPoints -= research.getCost();
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
                if (isInventoryContainsItem(inventory, new Pair(drop, new Object()))) {
                    int a = getInventoryItemIndex(inventory, drop);
                    inventory.set(a, new Pair<>(drop, inventory.get(a).second + 1));
                } else {
                    inventory.add(new Pair<>(drop, 1));
                }
            }
        }
        setGold(getGold() + enemy.getGivenGold());
        addExperience(enemy.getGivenExp());
    }

    public void castSpell() {
        chosenSpell.affect(enemy);
        Log.d("KKKGGGG", chosenSpell.getManaConsumption() + " " + getMana() + " " + getManaRegen());
    }

    public void choose_spell(Spell spell) {
        chosenSpell = spell;
    }

    public void equip(Item item) {
        if (item.getClass() == Weapon.class) {
            equipment.set(0, item);
            setDamage(getDamage() + ((Weapon) item).getDamage());
        } else if (item.getClass() == Armor.class) {
            Armor item1 = (Armor) item;
            switch (item1.getTypeOfArmor()) {
                case 1: {
                    if (equipment.get(((Armor) item).getTypeOfArmor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(1)).getArmor());
                    equipment.set(1, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 2: {
                    if (equipment.get(((Armor) item).getTypeOfArmor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(2)).getArmor());
                    equipment.set(2, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 3: {
                    if (equipment.get(((Armor) item).getTypeOfArmor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(3)).getArmor());
                    equipment.set(3, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 4: {
                    if (equipment.get(((Armor) item).getTypeOfArmor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(4)).getArmor());
                    equipment.set(4, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
                case 5: {
                    if (equipment.get(((Armor) item).getTypeOfArmor()) != null)
                        setArmor(getArmor() - ((Armor) equipment.get(5)).getArmor());
                    equipment.set(5, item1);
                    setArmor(getArmor() + item1.getArmor());
                    break;
                }
            }
        }
    }

    public boolean craft(Recipe recipe){
        ArrayList<Pair<Item, Integer>> ingredients=new ArrayList<>(recipe.getIngredients());
        for (int i=0;i<ingredients.size();i++){
            if (!isInventoryContainsItem(inventory, ingredients.get(i))||
                    inventory.get(getInventoryItemIndex(inventory, ingredients.get(i).first)).second<ingredients.get(i).second){
                return false;
            }
        }
        addItemToInventory(new Pair<>(recipe.getProduct(), 1));
        for (int i=0;i<ingredients.size();i++){
            removeItemFromInventory(ingredients.get(i));
        }
        return true;
    }

    public void eat(Food food) {
        setHealth(getHealth() + food.getHealthRecovery());
        setMana(getMana() + food.getManaRecovery());
        if (getHealth() > getMaxHealth())
            setHealth(getMaxHealth());
        if (getMana() > getMaxMana())
            setMana(getMaxMana());
    }

    public void attack() {
        getEnemy().take_damage(getDamage());
        if (enemy.isDuel())
            FirebaseDatabase.getInstance().getReference("Duel")
                    .child(getDuelNum()+"").child(1-getDuelPnum()+"").child("health").setValue(enemy.getHealth());
    }

    public void levelUp() {
        while (getExperience() >= getExperienceToNextLevelRequired()) {
            super.setLevel(this.getLevel() + 1);
            setExperience(getExperience() - getExperienceToNextLevelRequired());
            setExperienceToNextLevelRequired((int) Math.ceil(expFormula(getLevel())));
            researchPoints += 1;
            setMaxMana(getMana() * 1.3);
            setMana(getMaxMana());
            setMaxHealth(getMaxHealth() * 1.3);
            setHealth(getMaxHealth());
            setManaRegen(getManaRegen() * 1.4);
            setHealthRegen(getHealthRegen() * 1.4);
        }
    }

    public void addPlayerToDuel(DatabaseReference ref){
        ref.child("name").setValue(user.getLogin());
        ref.child("health").setValue(getHealth());
        ref.child("mana").setValue(getMana());
        ref.child("armor").setValue(getArmor());
        ref.child("damage").setValue(getDamage());
        ref.child("givenExp").setValue(getExperience());
        ref.child("givenGold").setValue(getGold());
        ref.child("maxMana").setValue(getMaxMana());
        ref.child("maxHealth").setValue(getMaxHealth());
        ref.child("ran").setValue(false);
        ref.child("dead").setValue(false);
    }

    private double expFormula(double x) {
        return ((Math.pow(x, Math.PI) - Math.pow(Math.PI, Math.E))
                * Math.cbrt(Math.pow(x, Math.E) - Math.PI))
                / (Math.pow(x, Math.E / 7));
    }

    public void addExperience(int exp) {
        setExperience(exp + getExperience());
        levelUp();
    }

    public void addGold(int gld){
        setGold(gld+getGold());
    }

    public ArrayList<Pair<Item, Integer>> getInventory() {
        return inventory;
    }

    public Bitmap getTitleTexture() {
        return titleTexture;
    }

    public void setTitleTexture(Bitmap titleTexture) {
        this.titleTexture = titleTexture;
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

    public int getResearchPoints() {
        return researchPoints;
    }

    public void setResearchPoints(int researchPoints) {
        this.researchPoints = researchPoints;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "gold=" + gold +
                ", experience=" + getExperience() +
                ", research_points=" + researchPoints +
                ", element_bonuses=" + elementBonuses +
                ", coordinates=" + coordinates +
                ", chosen_spell=" + chosenSpell +
                ", equipment=" + equipment +
                ", inventory=" + inventory +
                ", spells=" + spells +
                ", title_texture=" + titleTexture +
                ", enemy=" + enemy +
                '}';
    }

    public ArrayList<Integer> getElementBonuses() {
        return elementBonuses;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getChatMode() {
        return chatMode;
    }

    public void setChatMode(boolean chatMode) {
        this.chatMode = chatMode;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    private boolean isInventoryContainsItem(ArrayList<Pair<Item, Integer>> data, Pair<Item, Integer> element) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).first != null) {
                if (data.get(i).first.getName().equals(element.first.getName()))
                    return true;
            }
        }
        return false;
    }

    private int getInventoryItemIndex(ArrayList<Pair<Item, Integer>> data, Item element) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).first.getName().equals(element.getName()))
                return i;
        }
        return -1;
    }

    public void addItemToInventory(Pair<Item, Integer> item){
        if (isInventoryContainsItem(inventory, item))
            inventory.set(getInventoryItemIndex(inventory, item.first),
                    new Pair<>(item.first, inventory.get(getInventoryItemIndex(inventory, item.first)).second+1));
        else inventory.add(item);
    }

    private boolean removeItemFromInventory(Pair<Item, Integer> item){
        if (getInventoryItemIndex(inventory, item.first)!=-1){
            if (inventory.get(getInventoryItemIndex(inventory, item.first)).second-item.second<0){
                return false;
            }
            else {
                if (inventory.get(getInventoryItemIndex(inventory, item.first)).second-item.second==0){
                    inventory.remove(inventory.get(getInventoryItemIndex(inventory, item.first)));
                }else {
                    inventory.set(getInventoryItemIndex(inventory, item.first),
                            new Pair<>(item.first, inventory.get(getInventoryItemIndex(inventory, item.first)).second-item.second));
                }
            }
            return true;
        }
        return false;
    }

    public boolean sellItem(Pair<Item, Integer> item){
        if (removeItemFromInventory(item)){
            addGold(item.first.getCostSell());
            return true;
        }
        else return false;
    }

    public boolean buyItem(Pair<Item, Integer> item){
        if (gold<item.first.getCostBuy())
            return false;
        else {
            addItemToInventory(item);
            addGold(-item.first.getCostBuy());
            return true;
        }
    }

    public int getMapNum() {
        return mapNum;
    }

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }

    public Pair<Integer, Integer> getCoordinates(int mapNum) {
        return coordinates.get(mapNum);
    }

    public void setCoordinates(int mapNum, Pair<Integer, Integer> coordinates) {
        this.coordinates.set(mapNum, coordinates);
    }

    public int getDuelNum() {
        return duelNum;
    }

    public void setDuelNum(int duelNum) {
        this.duelNum = duelNum;
    }

    public int getDuelPnum() {
        return duelPnum;
    }

    public void setDuelPnum(int duelPnum) {
        this.duelPnum = duelPnum;
    }
}
