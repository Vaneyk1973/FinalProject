package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static Player player;
    public static HashMap<String, Integer> chances_of_fight =new HashMap<>();
    public static ArrayList<ArrayList<Integer>> map=new ArrayList<>();
    public static HashMap<String, HashMap<String, Integer>> chances_of_enemy=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player=new Player(R.id.m22);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, new Map());
        fragmentTransaction.add(R.id.status, new StatusBar());
        fragmentTransaction.add(R.id.menu, new Menu());
        fragmentTransaction.commit();

        chances_of_fight.put("mud_road", 50);
        chances_of_fight.put("forest", 75);
        chances_of_fight.put("fields", 30);
        chances_of_fight.put("king's_road", 10);
        chances_of_fight.put("stone_road", 30);
        chances_of_enemy.put("forest", new HashMap<>());
        chances_of_enemy.put("fields", new HashMap<>());
        chances_of_enemy.put("king's_road", new HashMap<>());
        chances_of_enemy.put("stone_road", new HashMap<>());
        chances_of_enemy.put("mud_road", new HashMap<>());
    }
}