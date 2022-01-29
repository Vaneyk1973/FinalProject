package com.example.finalproject.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.items.Recipe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CraftingStationFragment extends Fragment {
    private Recipe chosenRecipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crafting_station, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back=getView().findViewById(R.id.crafting_station_back_button),
                craft=getView().findViewById(R.id.craft_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fr=fm.beginTransaction();
                fr.remove(fm.findFragmentById(R.id.crafting_station));
                fr.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                fr.add(R.id.status, new StatusBarFragment());
                fr.add(R.id.menu, new MenuFragment());
                fr.commit();
            }
        });
        craft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenRecipe != null) {
                    if (!MainActivity.player.craft(chosenRecipe))
                        Toast.makeText(getContext(), "You don't have necessary ingredients", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getContext(), "Crafted successfully", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Choose recipe first", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView crafts=getView().findViewById(R.id.crafts);
        crafts.setLayoutManager(new LinearLayoutManager(getContext()));
        crafts.setAdapter(new CraftingAdapter(MainActivity.recipes));
    }

    private class CraftingAdapter extends RecyclerView.Adapter<CraftingAdapter.ViewHolder> {
        private ArrayList<Recipe> data=new ArrayList<>();

        public CraftingAdapter(ArrayList<Recipe> data) {
            this.data.addAll(data);
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.crafting_recipe, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull CraftingStationFragment.CraftingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(">"+data.get(position).getProduct().getName());
            String a="";
            for (int i=0;i<data.get(position).getIngredients().size()-1;i++)
                a+=data.get(position).getIngredients().get(i).first.getName()+" "+data.get(position).getIngredients().get(i).second+", ";
            a+=data.get(position).getIngredients().get(data.get(position).getIngredients().size()-1).first.getName()+" "+
                    data.get(position).getIngredients().get(data.get(position).getIngredients().size()-1).second;
            holder.ingredients.setText(a);
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm=getChildFragmentManager();
                    FragmentTransaction fr=fm.beginTransaction();
                    if (fm.findFragmentById(R.id.characteristics1)!=null)
                        fr.remove(fm.findFragmentById(R.id.characteristics1));
                    fr.add(R.id.characteristics1, new ItemCharacteristicsFragment(data.get(position).getProduct()));
                    fr.commit();
                    chosenRecipe=data.get(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            TextView name, ingredients;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.recipe_name);
                ingredients=itemView.findViewById(R.id.crafting_ingredients);
            }
        }
    }
}