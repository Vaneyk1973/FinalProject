package com.example.finalproject;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ResearchTree extends Fragment {
    private static boolean created=false;
    public static HashMap<Research, TextView> research_hash_map = new HashMap<>();
    public static HashMap<TextView, Research> research_hash_map1 = new HashMap<>();

    public static void setCreated(boolean created) {
        ResearchTree.created = created;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_research_tree, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!created){
            research_hash_map.put(MainActivity.researches.get(0), (TextView) getView().findViewById(R.id.basic_spell_creation));
            research_hash_map.put(MainActivity.researches.get(1), (TextView) getView().findViewById(R.id.fire_mage));
            research_hash_map.put(MainActivity.researches.get(2), (TextView) getView().findViewById(R.id.water_mage));
            research_hash_map.put(MainActivity.researches.get(3), (TextView) getView().findViewById(R.id.earth_mage));
            research_hash_map.put(MainActivity.researches.get(4), (TextView) getView().findViewById(R.id.air_mage));
            research_hash_map1.put((TextView) getView().findViewById(R.id.basic_spell_creation), MainActivity.researches.get(0));
            research_hash_map1.put((TextView) getView().findViewById(R.id.fire_mage), MainActivity.researches.get(1));
            research_hash_map1.put((TextView) getView().findViewById(R.id.water_mage), MainActivity.researches.get(2));
            research_hash_map1.put((TextView) getView().findViewById(R.id.earth_mage), MainActivity.researches.get(3));
            research_hash_map1.put((TextView) getView().findViewById(R.id.air_mage), MainActivity.researches.get(4));
            View.OnClickListener n = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.player.research(research_hash_map1.get(v));
                }
            };
            for (int i = 0; i < MainActivity.researches.size(); i++) {
                Log.d("KKTT", research_hash_map.get(MainActivity.researches.get(i))+"");
                research_hash_map.get(MainActivity.researches.get(i)).setText(MainActivity.researches.get(i).getName());
                research_hash_map.get(MainActivity.researches.get(i)).setOnClickListener(n);
                if (MainActivity.researches.get(i).isResearched())
                    research_hash_map.get(MainActivity.researches.get(i)).setBackgroundColor(Color.GREEN);
            }
            created=true;
        }
        Button back = getView().findViewById(R.id.back_button4);
        FragmentManager fm = getParentFragmentManager();
        TableLayout k = getView().findViewById(R.id.research_table);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.remove(fm.findFragmentById(R.id.research_tree));
                fragmentTransaction.add(R.id.map, new Map());
                fragmentTransaction.add(R.id.status, new StatusBar());
                fragmentTransaction.add(R.id.menu, new Menu());
                fragmentTransaction.commit();
            }
        });

    }
}