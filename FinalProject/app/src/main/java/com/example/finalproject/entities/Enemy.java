package com.example.finalproject.entities;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.R;
import com.example.finalproject.fragments.MainActivity;
import com.example.finalproject.fragments.MapFragment;
import com.example.finalproject.fragments.MenuFragment;
import com.example.finalproject.fragments.StatusBarFragment;
import com.example.finalproject.items.Item;
import com.example.finalproject.service.Triplex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Enemy extends Entity implements Parcelable {
    private ArrayList<Triplex<Item, Integer, Integer>> drop;
    private int defence = 0;
    private boolean t = true;
    private Bitmap texture;
    private boolean duel, dead;

    public Enemy(String name, int health, int mana, int damage, int armor, int givenGold, int givenExp, ArrayList<Triplex<Item, Integer, Integer>> drop, Bitmap b) {
        setHealthRegen(2);
        setArmor(armor);
        setDamage(damage);
        setHealth(health);
        setName(name);
        setMaxHealth(health);
        setMaxMana(mana);
        setDrop(drop);
        setGivenExp(givenExp);
        setGivenGold(givenGold);
        setTexture(b);
    }

    public Enemy(DatabaseReference ref, Bitmap b, TextView enemyHealth, TextView enemyMana, ProgressBar p){
        ref.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setName(task.getResult().getValue(String.class));
            }
        });
        ref.child("health").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setHealth(task.getResult().getValue(Double.class));
                ref.child("maxHealth").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()&&task.getResult().getValue()!=null)
                            setMaxHealth(task.getResult().getValue(Double.class));
                        enemyHealth.setText(Math.round(getHealth())+"/"+Math.round(getMaxHealth()));
                    }
                });
            }
        });
        ref.child("mana").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setMana(task.getResult().getValue(Double.class));
                ref.child("maxMana").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()&&task.getResult().getValue()!=null)
                            setMaxMana(task.getResult().getValue(Double.class));
                        enemyMana.setText(Math.round(getMana())+"/"+Math.round(getMaxMana()));
                    }
                });
            }
        });
        ref.child("damage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setDamage(task.getResult().getValue(Integer.class));
            }
        });
        ref.child("armor").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setArmor(task.getResult().getValue(Integer.class));
            }
        });
        ref.child("givenGold").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setGivenGold(task.getResult().getValue(Integer.class));
            }
        });
        ref.child("givenExp").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if (task.isSuccessful()&&task.getResult().getValue()!=null)
                    setGivenExp(task.getResult().getValue(Integer.class));
                p.setVisibility(View.GONE);
            }
        });
        ref.child("dead").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                MainActivity.player.addExperience(getGivenExp());
                MainActivity.player.addGold(getGivenGold());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        setDrop(new ArrayList<>());
        MainActivity.player.setEnemy(this);
        setTexture(b);
        duel=true;
    }

    public Enemy(Enemy enemy) {
        setHealthRegen(enemy.getHealthRegen());
        setManaRegen(enemy.getManaRegen());
        setArmor(enemy.getArmor());
        setDrop(enemy.getDrop());
        setDamage(enemy.getDamage());
        setExperience(enemy.getExperience());
        setExperienceToNextLevelRequired(enemy.getExperienceToNextLevelRequired());
        setHealth(enemy.getHealth());
        setLevel(enemy.getLevel());
        setMana(enemy.getMana());
        setMaxHealth(enemy.getMaxHealth());
        setMaxMana(enemy.getMaxMana());
        setName(enemy.getName());
        setPowerLevel(enemy.getPowerLevel());
        setGivenExp(enemy.getGivenExp());
        setGivenGold(enemy.getGivenGold());
        setTexture(enemy.getTexture());
    }

    public void attack(Player player) {
        player.take_damage(super.getDamage());
    }

    public void fight() {
        if (t) {
            attack(MainActivity.player);
            t = false;
        } else {
            defend();
            t = true;
        }
    }

    public void defend() {

    }

    public ArrayList<Triplex<Item, Integer, Integer>> getDrop() {
        return drop;
    }

    public void setDrop(ArrayList<Triplex<Item, Integer, Integer>> drop) {
        this.drop = drop;
    }

    public Bitmap getTexture() {
        return texture;
    }

    public void setTexture(Bitmap texture) {
        this.texture = texture;
    }

    public boolean isDuel() {
        return duel;
    }

    public void setDuel(boolean duel) {
        this.duel = duel;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }
}
