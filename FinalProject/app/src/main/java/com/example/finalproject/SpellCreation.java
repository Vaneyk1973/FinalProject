package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SpellCreation extends Fragment {
    private Element element=MainActivity.elements.get(0).first;
    private Type type=MainActivity.types.get(0).first;
    private Form form=MainActivity.forms.get(0).first;
    private ManaChannel mana_channel=MainActivity.manaChannels.get(0).first;
    private ManaReservoir mana_reservoir=MainActivity.manaReservoirs.get(0).first;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spell_creation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final String[] spell_name = new String[1];
        EditText name=(EditText)getView().findViewById(R.id.spell_name);
        Button confirm_spell=(Button)getView().findViewById(R.id.confirm_creation);
        confirm_spell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.player.getSpells().add(new Spell(element, type, form, mana_channel, mana_reservoir, name.getText().toString()));
            }
        });
        Button back=(Button)getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fragmentTransaction= fm.beginTransaction();
                fragmentTransaction.remove(fm.findFragmentById(R.id.spell_creation));
                fragmentTransaction.add(R.id.map, new Map());
                fragmentTransaction.add(R.id.status, new StatusBar());
                fragmentTransaction.add(R.id.menu, new Menu());
                fragmentTransaction.commit();
            }
        });
        Bitmap bm=Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.YELLOW);
        ImageView element_view=(ImageView)getView().findViewById(R.id.element);
        element_view.setImageBitmap(bm);
        ImageView mana_reservoir_view=(ImageView)getView().findViewById(R.id.mana_reservoir);
        mana_reservoir_view.setImageBitmap(bm);
        ImageView type_view=(ImageView)getView().findViewById(R.id.type);
        type_view.setImageBitmap(bm);
        ImageView mana_channel_view=(ImageView)getView().findViewById(R.id.mana_channel);
        mana_channel_view.setImageBitmap(bm);
        ImageView form_view=(ImageView)getView().findViewById(R.id.form);
        form_view.setImageBitmap(bm);
        RecyclerView comps=getView().findViewById(R.id.avaliable_components);
        comps.setLayoutManager(new LinearLayoutManager(getContext()));
        element_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter<>(MainActivity.elements));
            }
        });
        mana_reservoir_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter<>(MainActivity.manaReservoirs));
            }
        });
        mana_channel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter<>(MainActivity.manaChannels));
            }
        });
        type_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter<>(MainActivity.types));
            }
        });
        form_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter<>(MainActivity.forms));
            }
        });
    }

    class SpellAdapter<T extends Component> extends RecyclerView.Adapter<SpellAdapter.ViewHolder>{
        private final ArrayList<Pair<T, Boolean>> data=new ArrayList<>();

        public SpellAdapter(ArrayList<Pair<T, Boolean>> data) {
            this.data.addAll(data);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView comp;
            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                comp=(TextView)itemView.findViewById(R.id.comp);
            }
        }

        @NonNull
        @NotNull
        @Override
        public SpellCreation.SpellAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.components_item, parent, false);
            return new SpellAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SpellCreation.SpellAdapter.ViewHolder holder, int position) {
            Class r=data.get(position).first.getClass();
            holder.comp.setText(String.valueOf(data.get(position).first.getName()));
            if (r.equals(Element.class)) {
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        element=(Element)data.get(position).first;
                    }
                });
            }
            else if (r.equals(Type.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type=(Type)data.get(position).first;
                    }
                });
            }
            else if (r.equals(ManaReservoir.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mana_reservoir=(ManaReservoir)data.get(position).first;
                    }
                });
            }
            else if (r.equals(ManaChannel.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mana_channel=(ManaChannel)data.get(position).first;
                    }
                });
            }
            else if (r.equals(Form.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form=(Form)data.get(position).first;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}