package com.example.finalproject.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.fragments.MainActivity;
import com.example.finalproject.fragments.ResearchTreeFragment;
import com.example.finalproject.fragments.ShopFragment;
import com.example.finalproject.fragments.StatusBarFragment;
import com.example.finalproject.items.Armor;
import com.example.finalproject.items.Food;
import com.example.finalproject.items.Item;
import com.example.finalproject.items.Recipe;
import com.example.finalproject.items.Weapon;
import com.example.finalproject.service.Research;
import com.example.finalproject.service.Triplex;
import com.example.finalproject.service.User;
import com.example.finalproject.service.spell.Spell;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dalvik.system.InMemoryDexClassLoader;

public class Player extends Entity implements Parcelable {

    private int gold, researchPoints, mapNum, duelNum, duelPnum;
    private boolean chatMode;
    private User user;
    private ArrayList<Integer> elementBonuses = new ArrayList<>();
    private ArrayList<Item> equipment = new ArrayList<>();
    private ArrayList<Pair<Item, Integer>> inventory = new ArrayList<>();
    private ArrayList<Spell> spells = new ArrayList<>();
    private ArrayList<Pair<Integer, Integer>> coordinates = new ArrayList<>();
    private HashMap<Integer, Integer> itemsOnAuction=new HashMap<>();
    private Spell chosenSpell;
    private Bitmap titleTexture;
    private Bitmap avatar;
    private Enemy enemy;

    protected Player(Parcel in) {
        super(in);
        gold = in.readInt();
        researchPoints = in.readInt();
        mapNum=in.readInt();
        duelNum=in.readInt();
        duelPnum=in.readInt();
        chatMode = new Boolean(in.readString());
        user=new Gson().fromJson(in.readString(), User.class);
        elementBonuses=in.readArrayList(new GenericTypeIndicator<ArrayList<Integer>>(){}.getClass().getClassLoader());
        equipment=in.readArrayList(new GenericTypeIndicator<ArrayList<Item>>(){}.getClass().getClassLoader());
        inventory=in.readArrayList(new GenericTypeIndicator<ArrayList<Pair<Item, Integer>>>(){}.getClass().getClassLoader());
        spells=in.readArrayList(new GenericTypeIndicator<ArrayList<Spell>>(){}.getClass().getClassLoader());
        coordinates=in.readArrayList(new GenericTypeIndicator<ArrayList<Pair<Integer, Integer>>>(){}.getClass().getClassLoader());
        itemsOnAuction=in.readHashMap(new GenericTypeIndicator<HashMap<Integer, Integer>>(){}.getClass().getClassLoader());
        chosenSpell= (Spell) in.readSerializable();
        titleTexture=in.readParcelable(Bitmap.class.getClassLoader());
        avatar=in.readParcelable(Bitmap.class.getClassLoader());
        enemy=in.readParcelable(Enemy.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(gold);
        dest.writeInt(researchPoints);
        dest.writeInt(mapNum);
        dest.writeInt(duelNum);
        dest.writeInt(duelPnum);
        dest.writeString(chatMode + "");
        dest.writeString(new Gson().toJson(user));
        dest.writeSerializable(elementBonuses);
        dest.writeSerializable(equipment);
        dest.writeParcelable((Parcelable) inventory, flags);
        dest.writeSerializable(spells);
        dest.writeParcelable((Parcelable) coordinates, flags);
        dest.writeSerializable(itemsOnAuction);
        dest.writeSerializable(chosenSpell);
        dest.writeParcelable(titleTexture, flags);
        dest.writeParcelable(avatar, flags);
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
                Log.d("WWD", new Gson().toJson(drop)+"F");
                if (isInventoryContainsItem(drop)) {
                    int a = getInventoryItemIndex(drop);
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

    public boolean craft(Recipe recipe) {
        ArrayList<Pair<Item, Integer>> ingredients = new ArrayList<>(recipe.getIngredients());
        for (int i = 0; i < ingredients.size(); i++) {
            if (!isInventoryContainsItem(ingredients.get(i).first) ||
                    inventory.get(getInventoryItemIndex(ingredients.get(i).first)).second < ingredients.get(i).second) {
                return false;
            }
        }
        addItemToInventory(new Pair<>(recipe.getProduct(), 1));
        for (int i = 0; i < ingredients.size(); i++) {
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
                    .child(getDuelNum() + "").child(1 - getDuelPnum() + "").child("health").setValue(enemy.getHealth());
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
            checkTasks();
        }
    }

    public void addPlayerToDuel(DatabaseReference ref) {
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

    public void addGold(int gld) {
        setGold(gld + getGold());
        checkTasks();
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

    private boolean isInventoryContainsItem(Item item) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).first != null) {
                Log.d("WW", new Gson().toJson(inventory.get(i)));
                Log.d("WWF", new Gson().toJson(item.getName()));
                if (inventory.get(i).first.getName().equals(item.getName()))
                    return true;
            }
        }
        return false;
    }

    private int getInventoryItemIndex(Item element) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).first.getName().equals(element.getName()))
                return i;
        }
        return -1;
    }

    public void addItemToInventory(Pair<Item, Integer> item) {
        if (isInventoryContainsItem(item.first))
            inventory.set(getInventoryItemIndex(item.first),
                    new Pair<>(item.first, inventory.get(getInventoryItemIndex(item.first)).second + item.second));
        else inventory.add(item);
    }

    private boolean removeItemFromInventory(Pair<Item, Integer> item) {
        if (getInventoryItemIndex(item.first) != -1) {
            if (inventory.get(getInventoryItemIndex(item.first)).second < item.second) {
                return false;
            } else {
                if (getAmountOfItemsInInventory(item.first) == item.second) {
                    inventory.remove(inventory.get(getInventoryItemIndex(item.first)));
                } else {
                    inventory.set(getInventoryItemIndex(item.first),
                            new Pair<>(item.first, inventory.get(getInventoryItemIndex(item.first)).second - item.second));
                }
            }
            return true;
        }
        return false;
    }

    public boolean sellItem(Pair<Item, Integer> item) {
        if (removeItemFromInventory(item)) {
            addGold(item.first.getCostSell());
            return true;
        } else return false;
    }

    public boolean buyItem(Pair<Item, Integer> item) {
        if (gold < item.first.getCostBuy())
            return false;
        else {
            addItemToInventory(item);
            addGold(-item.first.getCostBuy()*item.second);
            return true;
        }
    }

    public int getAmountOfItemsInInventory(Item item) {
        if (!isInventoryContainsItem(item))
            return 0;
        else return inventory.get(getInventoryItemIndex(item)).second;
    }

    public void placeItemOnAuction(Triplex<Item, Integer, String> item, RecyclerView shopList) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Auction");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean t = false;
                    if (getAmountOfItemsInInventory(item.first) >= item.second) {
                        ArrayList<Triplex<Item, Integer, String>> a
                                = task.getResult().getValue(new GenericTypeIndicator<ArrayList<Triplex<Item, Integer, String>>>() {
                        });
                        if (a != null)
                            for (int i = 0; i < a.size(); i++) {
                                if (a.get(i).first.getName().equals(item.first.getName()) && a.get(i).third.equals(item.third)) {
                                    ref.child(i + "").child("second").setValue(item.second + a.get(i).second);
                                    int finalI = i;
                                    ref.child(i+"").child("pair").child("second").setValue(item.second + a.get(i).second).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            itemsOnAuction.replace(finalI, item.second);
                                        }
                                    });
                                    t = true;
                                    removeItemFromInventory(item.getPair());
                                    shopList.setAdapter(new ShopFragment.ShopAdapter(inventory));
                                    break;
                                }
                            }
                        if (!t){
                            int r=Integer.parseInt(task.getResult().getChildrenCount()+"");
                            ref.child(r + "").setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        removeItemFromInventory(item.getPair());
                                        itemsOnAuction.put(r, item.second);
                                        shopList.setAdapter(new ShopFragment.ShopAdapter(inventory));
                                        ref.child(r+"").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                if (snapshot.getValue()!=null){
                                                    Pair<Item, Integer> item =new Pair<>(snapshot.child("first").getValue(Item.class),
                                                            snapshot.child("second").getValue(int.class));
                                                    if (item.second <itemsOnAuction.get(r)){
                                                        addGold((itemsOnAuction.get(r)-item.second)*item.first.getCostBuy());
                                                        StatusBarFragment.update();
                                                        itemsOnAuction.replace(r, item.second);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void buyItemOnAuction(int amount, String name, String user, RecyclerView shopList, Context context) {
        final Pair[] item = new Pair[1];
        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Auction");
        ref1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                int position = 0;
                ArrayList<Triplex<Item, Integer, String>> a
                        = task.getResult().getValue(new GenericTypeIndicator<ArrayList<Triplex<Item, Integer, String>>>() {
                });
                for (int i = 0; i < a.size(); i++) {
                    if (a.get(i).first.getName().equals(name) && a.get(i).third.equals(user))
                        position = i;
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Auction").child(position + "");
                int finalPosition = position;
                ref.child("pair").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            item[0] = new Pair(task.getResult().child("first").getValue(Item.class)
                                    ,task.getResult().child("second").getValue(int.class));
                            Log.d("KKKl", item[0].second+" "+amount+" "+ finalPosition);
                            if (amount * ((Item) item[0].first).getCostBuy() > gold)
                                Toast.makeText(context, "You don't have enough gold", Toast.LENGTH_SHORT).show();
                            else if (amount > (int) item[0].second)
                                Toast.makeText(context, "You want to buy too many items", Toast.LENGTH_SHORT).show();
                            else{
                                ref.child("second").setValue((int) item[0].second - amount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseDatabase.getInstance().getReference("Auction").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        shopList.setAdapter(new ShopFragment.ShopAdapter(
                                                                task.getResult().getValue(
                                                                        new GenericTypeIndicator<ArrayList<Triplex<Item, Integer, String>>>(){}),
                                                                true));
                                                        addGold(-(int) ((Item) item[0].first).getCostBuy() * amount);
                                                        addItemToInventory(new Pair<>((Item) item[0].first, amount));
                                                        Toast.makeText(context, "Bought successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                ref.child("pair").child("second").setValue((int) item[0].second - amount);
                            }
                        }
                    }
                });
            }
        });
    }

    public void checkTasks(){
        if (gold>=100&&MainActivity.tasks.get(0).isTaken()&&!MainActivity.tasks.get(0).isCompleted()){
            MainActivity.tasks.get(0).setCompleted(true);
            addGold(50);
            addExperience(50);
        }
        if (getLevel()>=5&&MainActivity.tasks.get(1).isTaken()&&!MainActivity.tasks.get(1).isCompleted()){
            MainActivity.tasks.get(1).setCompleted(true);
            addGold(500);
            addExperience(100);
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
