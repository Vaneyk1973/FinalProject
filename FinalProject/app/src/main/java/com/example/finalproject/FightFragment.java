package com.example.finalproject;

import android.app.slice.SliceItem;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class FightFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fight, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        MainActivity.player.setEnemy(new Enemy(MainActivity.enemies.get(0)));
        Button run = (Button) getView().findViewById(R.id.run);
        Button attack = (Button) getView().findViewById(R.id.attack);
        Button cast_spell = (Button) getView().findViewById(R.id.cast_spell);
        RecyclerView spells = (RecyclerView) getView().findViewById(R.id.avaliable_spells);
        spells.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView player_image = (ImageView) getView().findViewById(R.id.player);
        ImageView enemy_image = (ImageView) getView().findViewById(R.id.enemy);

        ProgressBar your_health = getView().findViewById(R.id.your_health),
                your_mana = getView().findViewById(R.id.your_mana),
                enemy_health = getView().findViewById(R.id.enemy_health),
                enemy_mana = getView().findViewById(R.id.enemy_mana);

        your_health.setMax(MainActivity.player.getMax_health());
        your_health.setProgress(MainActivity.player.getHealth());
        your_mana.setMax(MainActivity.player.getMax_mana());
        your_mana.setProgress(MainActivity.player.getMana());

        enemy_health.setMax(MainActivity.player.getEnemy().getMax_health());
        enemy_health.setProgress(MainActivity.player.getEnemy().getHealth());
        enemy_mana.setMax(MainActivity.player.getEnemy().getMax_mana());
        enemy_mana.setProgress(MainActivity.player.getEnemy().getMana());

        Bitmap bm = Bitmap.createBitmap(width / 4, width / 4, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.GREEN);
        player_image.setImageBitmap(Bitmap.createBitmap(bm));
        bm.eraseColor(Color.RED);
        enemy_image.setImageBitmap(Bitmap.createBitmap(bm));
        your_health.setProgressTintList(ColorStateList.valueOf(Color.RED));
        your_mana.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        enemy_health.setProgressTintList(ColorStateList.valueOf(Color.RED));
        enemy_mana.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.player.regenerate();
                MainActivity.player.getEnemy().regenerate();
                MainActivity.player.attack();
                MainActivity.player.getEnemy().fight();
                enemy_health.setProgress(MainActivity.player.getEnemy().getHealth());
                your_health.setProgress(MainActivity.player.getHealth());
                if (MainActivity.player.getEnemy().getHealth() <= 0) {
                    MainActivity.player.take_drop();
                    FragmentManager fm = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                    fragmentTransaction.add(R.id.map, new Map());
                    fragmentTransaction.add(R.id.status, new StatusBar());
                    fragmentTransaction.add(R.id.menu, new Menu());
                    fragmentTransaction.commit();
                }
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = new Random().nextInt(100);
                if (a >= 50) {
                    FragmentManager fm = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                    fragmentTransaction.add(R.id.map, new Map());
                    fragmentTransaction.add(R.id.status, new StatusBar());
                    fragmentTransaction.add(R.id.menu, new Menu());
                    fragmentTransaction.commit();
                }
            }
        });
        cast_spell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spells.setAdapter(new SpellsAdapter(MainActivity.player.getSpells()));
            }
        });
    }

    class SpellsAdapter extends RecyclerView.Adapter<SpellsAdapter.SpellViewHolder> {
        ArrayList<Spell> data = new ArrayList<>();

        public SpellsAdapter(ArrayList<Spell> data) {
            this.data.addAll(data);
        }

        @NonNull
        @NotNull
        @Override
        public SpellViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new SpellViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spells_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull FightFragment.SpellsAdapter.SpellViewHolder holder, int position) {
            holder.name.setText(data.get(position).getName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.player.choose_spell(data.get(position));
                    ((RecyclerView) getView().findViewById(R.id.avaliable_spells)).setAdapter(new SpellsAdapter(new ArrayList<>()));
                    MainActivity.player.cast_spell();
                    ProgressBar your_health = getView().findViewById(R.id.your_health),
                            your_mana = getView().findViewById(R.id.your_mana),
                            enemy_health = getView().findViewById(R.id.enemy_health),
                            enemy_mana = getView().findViewById(R.id.enemy_mana);
                    enemy_health.setProgress(MainActivity.player.getEnemy().getHealth());
                    your_health.setProgress(MainActivity.player.getHealth());
                    your_mana.setProgress(MainActivity.player.getMana());
                    if (MainActivity.player.getEnemy().getHealth() <= 0) {
                        MainActivity.player.take_drop();
                        FragmentManager fm = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                        fragmentTransaction.add(R.id.map, new Map());
                        fragmentTransaction.add(R.id.status, new StatusBar());
                        fragmentTransaction.add(R.id.menu, new Menu());
                        fragmentTransaction.commit();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class SpellViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public SpellViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.spell_in_list);
            }
        }
    }
}



