package com.example.finalproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.R;
import com.example.finalproject.service.Research;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ResearchTreeFragment extends Fragment {
    public static HashMap<Research, TextView> research_hash_map;
    public static HashMap<TextView, Research> research_hash_map1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_research_tree, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        research_hash_map=new HashMap<>();
        research_hash_map1=new HashMap<>();
        Log.d("KKKLLL", new Gson().toJson(MainActivity.researches));
        Log.d("KKKLL!", new Gson().toJson(MainActivity.researches1));
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
        TextView rpa=getView().findViewById(R.id.research_points_amount);
        rpa.setText("Your research points amount: "+
                MainActivity.player.getResearch_points()+"");
        View.OnClickListener n = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.player.research(research_hash_map1.get(v));
                rpa.setText("Your research points amount: "+
                        MainActivity.player.getResearch_points()+"");
                MainActivity.researches1.clear();
                for (int i=0;i<MainActivity.researches.size();i++)
                    MainActivity.researches1.add(new Gson().toJson(MainActivity.researches.get(i)));
                MainActivity.elements1.clear();
                for (int i=0;i<MainActivity.elements.size();i++)
                    MainActivity.elements1.add(new Gson().toJson(MainActivity.elements.get(i)));
                MainActivity.forms1.clear();
                for (int i=0;i<MainActivity.forms.size();i++)
                    MainActivity.forms1.add(new Gson().toJson(MainActivity.forms.get(i)));
                MainActivity.types1.clear();
                for (int i=0;i<MainActivity.types.size();i++)
                    MainActivity.types1.add(new Gson().toJson(MainActivity.types.get(i)));
                MainActivity.mana_channels1.clear();
                for (int i=0;i<MainActivity.mana_channels.size();i++)
                    MainActivity.mana_channels1.add(new Gson().toJson(MainActivity.mana_channels.get(i)));
                MainActivity.mana_reservoirs1.clear();
                for (int i=0;i<MainActivity.mana_reservoirs.size();i++)
                    MainActivity.mana_reservoirs1.add(new Gson().toJson(MainActivity.mana_reservoirs.get(i)));
                Log.d("KKKLLL", new Gson().toJson(MainActivity.researches));
                Log.d("KKKLL!", new Gson().toJson(MainActivity.researches1));
            }
        };
        for (int i = 0; i < MainActivity.researches.size(); i++) {
            research_hash_map.get(MainActivity.researches.get(i)).setText(MainActivity.researches.get(i).getName()+
                    " : " +MainActivity.researches.get(i).getCost());
            research_hash_map.get(MainActivity.researches.get(i)).setOnClickListener(n);
            if (MainActivity.researches.get(i).isResearched())
                research_hash_map.get(MainActivity.researches.get(i)).setBackgroundColor(Color.GREEN);
        }
        Button back = getView().findViewById(R.id.back_button4);
        FragmentManager fm = getParentFragmentManager();
        TableLayout k = getView().findViewById(R.id.research_table);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.remove(fm.findFragmentById(R.id.research_tree));
                fragmentTransaction.add(R.id.map, new MapFragment());
                fragmentTransaction.add(R.id.status, new StatusBarFragment());
                fragmentTransaction.add(R.id.menu, new MenuFragment());
                fragmentTransaction.commit();
            }
        });

    }
}