package com.example.finalproject.fragments;

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

import com.example.finalproject.R;
import com.example.finalproject.service.spell.Spell;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SpellsFragment extends Fragment {

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
        Button back=(Button)getView().findViewById(R.id.spells_back_button);
        FragmentManager f=getParentFragmentManager();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction= f.beginTransaction();
                fragmentTransaction.remove(f.findFragmentById(R.id.spells));
                fragmentTransaction.add(R.id.map, new MapFragment());
                fragmentTransaction.add(R.id.status, new StatusBarFragment());
                fragmentTransaction.add(R.id.menu, new MenuFragment());
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
        public void onBindViewHolder(@NonNull @NotNull SpellsFragment.SpellsAdapter.SpellsViewHolder holder, int position) {
            holder.name.setText(spells.get(position).getName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm=getChildFragmentManager();
                    FragmentTransaction fr=fm.beginTransaction();
                    if (fm.findFragmentById(R.id.spells_char)!=null)
                        fr.remove(fm.findFragmentById(R.id.spells_char));
                    fr.add(R.id.spells_char, new SpellCharacteristicsFragment(spells.get(position)));
                    fr.commit();
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