package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Spells extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spells, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView spells=(RecyclerView)getView().findViewById(R.id.spells_list);
        spells.setLayoutManager(new LinearLayoutManager(getContext()));
        spells.setAdapter(new SpellsAdapter(MainActivity.player.getSpells()));
        Button back=(Button)getView().findViewById(R.id.back_button2);
        FragmentManager f=getParentFragmentManager();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction= f.beginTransaction();
                fragmentTransaction.remove(f.findFragmentById(R.id.spells));
                fragmentTransaction.add(R.id.map, new Map());
                fragmentTransaction.add(R.id.status, new StatusBar());
                fragmentTransaction.add(R.id.menu, new Menu());
                fragmentTransaction.commit();
            }
        });
    }

    class SpellsAdapter extends RecyclerView.Adapter<SpellsAdapter.SpellsViewHolder> {
        ArrayList<Spell> spells;

        public SpellsAdapter(ArrayList<Spell> spells) {
            this.spells = spells;
        }

        @NonNull
        @NotNull
        @Override
        public SpellsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new SpellsViewHolder((View)LayoutInflater.from(parent.getContext()).inflate(R.layout.spells_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull Spells.SpellsAdapter.SpellsViewHolder holder, int position) {
            holder.name.setText(spells.get(position).getName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TextView)(getView().findViewById(R.id.spell_name_char))).
                            setText(spells.get(position).getName());
                    ((TextView)(getView().findViewById(R.id.spell_type_char))).
                            setText(spells.get(position).getType().getName());
                    ((TextView)(getView().findViewById(R.id.spell_element_char))).
                            setText(spells.get(position).getElement().getName());
                    ((TextView)(getView().findViewById(R.id.spell_damage_char))).
                            setText(String.valueOf(spells.get(position).getDamage()));
                    ((TextView)(getView().findViewById(R.id.spell_mana_consumption_char))).
                            setText(String.valueOf(spells.get(position).getMana_consumption()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return spells.size();
        }

        class SpellsViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            public SpellsViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.spell_in_list);
            }
        }
    }
}