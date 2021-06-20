package com.example.finalproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.entities.Enemy;
import com.example.finalproject.entities.Player;
import com.example.finalproject.service.User;
import com.example.finalproject.service.spell.Spell;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class FightFragment extends Fragment {
    private boolean duel;

    public FightFragment(boolean duel) {
        this.duel = duel;
    }

    public FightFragment() {
        duel = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentManager fm = getParentFragmentManager();
        ProgressBar p = getView().findViewById(R.id.fight_progress_bar);
        p.setVisibility(View.GONE);
        MainActivity.m.start(getContext(), R.raw.fight);
        Button run = (Button) getView().findViewById(R.id.run);
        Button attack = (Button) getView().findViewById(R.id.attack);
        Button castSpell = (Button) getView().findViewById(R.id.cast_spell);
        RecyclerView spells = (RecyclerView) getView().findViewById(R.id.avaliable_spells);
        spells.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView playerImage = (ImageView) getView().findViewById(R.id.player);
        ImageView enemyImage = (ImageView) getView().findViewById(R.id.enemy);

        TextView yourHealth = getView().findViewById(R.id.your_health),
                yourMana = getView().findViewById(R.id.your_mana),
                enemyHealth = getView().findViewById(R.id.enemy_health),
                enemyMana = getView().findViewById(R.id.enemy_mana);

        if (duel) {
            p.setVisibility(View.VISIBLE);
            DatabaseReference[] databaseReferences = new DatabaseReference[2];
            Enemy[] enemies = new Enemy[1];
            ValueEventListener player = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    MainActivity.player.setHealth(snapshot.child("health").getValue(Double.class));
                    MainActivity.player.setMana(snapshot.child("mana").getValue(Double.class));
                    yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                    yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                    if (MainActivity.player.getHealth() <= 0) {
                        snapshot.getRef().child("dead").setValue(true);
                        MainActivity.player.setGold(0);
                        MainActivity.player.setExperience(0);
                        run.callOnClick();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            },
                    enemy = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null)
                                enemies[0] = new Enemy(snapshot.getRef(), MainActivity.b[5][6], enemyHealth, enemyMana, p);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    };
            FirebaseDatabase.getInstance().getReference("Duel")
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if (task.getResult().child(task.getResult().getChildrenCount() - 1 + "").getChildrenCount() == 1) {
                        p.animate();
                        databaseReferences[1] = task.getResult().child(task.getResult().getChildrenCount() - 1 + "").getRef().child("0");
                        databaseReferences[0] = task.getResult().child(task.getResult().getChildrenCount() - 1 + "").getRef().child("1");
                        MainActivity.player.addPlayerToDuel(databaseReferences[0]);
                        MainActivity.player.setDuelNum(Integer.parseInt(task.getResult().getChildrenCount() - 1 + ""));
                        MainActivity.player.setDuelPnum(1);
                        databaseReferences[1].addValueEventListener(enemy);
                        databaseReferences[0].addValueEventListener(player);
                        databaseReferences[1].child("ran").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.getValue(boolean.class)) {
                                    run.callOnClick();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        enemies[0] = new Enemy(databaseReferences[1], MainActivity.b[5][6], enemyHealth, enemyMana, p);
                        yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                        yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                        run.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReferences[0].child("ran").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                        if (!task.getResult().getValue(boolean.class)) {
                                            databaseReferences[0].child("ran").setValue(true);
                                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                            fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                                            fragmentTransaction.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                                            fragmentTransaction.add(R.id.status, new StatusBarFragment());
                                            fragmentTransaction.add(R.id.menu, new MenuFragment());
                                            fragmentTransaction.commit();
                                        }
                                    }
                                });
                            }
                        });
                        attack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MainActivity.player.attack();
                            }
                        });
                    } else {
                        int a = Integer.parseInt(task.getResult().getChildrenCount() + "");
                        databaseReferences[0] = task.getResult().child(a + "").getRef().child("0");
                        MainActivity.player.addPlayerToDuel(databaseReferences[0]);
                        MainActivity.player.setDuelNum(Integer.parseInt(a + ""));
                        MainActivity.player.setDuelPnum(0);
                        yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                        yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                        run.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReferences[0].child("ran").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                        if (!task.getResult().getValue(boolean.class)) {
                                            databaseReferences[0].child("ran").setValue(true);
                                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                            fragmentTransaction.remove(getParentFragmentManager().findFragmentById(R.id.fight));
                                            fragmentTransaction.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                                            fragmentTransaction.add(R.id.status, new StatusBarFragment());
                                            fragmentTransaction.add(R.id.menu, new MenuFragment());
                                            fragmentTransaction.commit();
                                        }
                                    }
                                });
                            }
                        });
                        attack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                task.getResult().child(task.getResult().getChildrenCount() + "")
                                        .getRef().child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()&&task.getResult().getValue()!=null)
                                            MainActivity.player.attack();
                                    }
                                });
                            }
                        });
                        databaseReferences[0].addValueEventListener(player);
                        task.getResult().child(task.getResult().getChildrenCount() + "")
                                .getRef().child("1").addValueEventListener(enemy);
                        task.getResult().child(task.getResult().getChildrenCount() + "")
                                .getRef().child("1").child("ran").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null && snapshot.getValue(boolean.class)) {
                                    run.callOnClick();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        p.animate();
                    }
                }
            });

            playerImage.setImageBitmap(MainActivity.b[5][5]);
            enemyImage.setImageBitmap(MainActivity.b[5][6]);
        } else {
            playerImage.setImageBitmap(MainActivity.b[5][5]);
            enemyImage.setImageBitmap(MainActivity.player.getEnemy().getTexture());
            yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
            yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
            enemyHealth.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
            enemyMana.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
            attack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.player.regenerate();
                    MainActivity.player.getEnemy().regenerate();
                    MainActivity.player.attack();
                    MainActivity.player.getEnemy().fight();
                    yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                    yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                    enemyHealth.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    enemyMana.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    if (MainActivity.player.getEnemy().getHealth() <= 0) {
                        MainActivity.player.take_drop();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                        fragmentTransaction.add(R.id.map, new MapFragment());
                        fragmentTransaction.add(R.id.status, new StatusBarFragment());
                        fragmentTransaction.add(R.id.menu, new MenuFragment());
                        fragmentTransaction.commit();
                        MainActivity.m.start(getContext(), R.raw.main);
                    }
                    if (MainActivity.player.getHealth() <= 0) {
                        MainActivity.player = new Player(2, 2);
                        MainActivity.setInitialData();
                        Toast.makeText(getContext(), "You died \n All of your progress will be deleted \n Better luck this time", Toast.LENGTH_LONG).show();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                        fragmentTransaction.add(R.id.map, new MapFragment());
                        fragmentTransaction.add(R.id.status, new StatusBarFragment());
                        fragmentTransaction.add(R.id.menu, new MenuFragment());
                        fragmentTransaction.commit();
                        MainActivity.m.start(getContext(), R.raw.main);
                    }
                }
            });
            run.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = new Random().nextInt(100);
                    MainActivity.player.regenerate();
                    MainActivity.player.getEnemy().regenerate();
                    MainActivity.player.getEnemy().fight();
                    Log.d("KK", MainActivity.player.getEnemy().getName());
                    yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                    yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                    enemyHealth.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    enemyMana.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    if (a >= 50) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                        fragmentTransaction.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                        fragmentTransaction.add(R.id.status, new StatusBarFragment());
                        fragmentTransaction.add(R.id.menu, new MenuFragment());
                        fragmentTransaction.commit();
                    }
                }
            });
            castSpell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("KK", MainActivity.player.getEnemy().getName());
                    yourHealth.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                    yourMana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                    enemyHealth.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    enemyMana.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    spells.setAdapter(new SpellsAdapter(MainActivity.player.getSpells()));
                }
            });
        }
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
                    if (MainActivity.player.getMana() < data.get(position).getManaConsumption())
                        Toast.makeText(getContext(), "Not enough mana", Toast.LENGTH_SHORT).show();
                    MainActivity.player.castSpell();
                    MainActivity.player.regenerate();
                    MainActivity.player.getEnemy().regenerate();
                    MainActivity.player.getEnemy().fight();
                    TextView your_health = getView().findViewById(R.id.your_health),
                            your_mana = getView().findViewById(R.id.your_mana),
                            enemy_health = getView().findViewById(R.id.enemy_health),
                            enemy_mana = getView().findViewById(R.id.enemy_mana);
                    your_health.setText(Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMaxHealth()));
                    your_mana.setText(Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMaxMana()));
                    enemy_health.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    enemy_mana.setText(MainActivity.player.getEnemy().getHealth() + "/" + MainActivity.player.getEnemy().getMaxHealth());
                    if (MainActivity.player.getEnemy().getHealth() <= 0) {
                        MainActivity.player.take_drop();
                        FragmentManager fm = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.remove(fm.findFragmentById(R.id.fight));
                        fragmentTransaction.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                        fragmentTransaction.add(R.id.status, new StatusBarFragment());
                        fragmentTransaction.add(R.id.menu, new MenuFragment());
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



