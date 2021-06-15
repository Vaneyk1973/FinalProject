package com.example.finalproject.fragments;

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

import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.service.spell.Component;
import com.example.finalproject.service.spell.Element;
import com.example.finalproject.service.spell.Form;
import com.example.finalproject.service.spell.ManaChannel;
import com.example.finalproject.service.spell.ManaReservoir;
import com.example.finalproject.R;
import com.example.finalproject.service.spell.Spell;
import com.example.finalproject.service.spell.Type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SpellCreationFragment extends Fragment {
    private Element element=MainActivity.elements.get(0);
    private Type type=MainActivity.types.get(0);
    private Form form=MainActivity.forms.get(0);
    private ManaChannel mana_channel=MainActivity.manaChannels.get(0);
    private ManaReservoir mana_reservoir=MainActivity.manaReservoirs.get(0);
    private String name;

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
        EditText name_view=(EditText)getView().findViewById(R.id.spell_name);
        name_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                name=v.getText().toString();
                return false;
            }
        });
        Button confirm_spell=(Button)getView().findViewById(R.id.confirm_creation);
        name=name_view.getText().toString();
        confirm_spell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=name_view.getText().toString();
                MainActivity.player.getSpells().add(new Spell(element, type, form, mana_channel, mana_reservoir, name));

            }
        });
        Button back=(Button)getView().findViewById(R.id.spell_creation_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fragmentTransaction= fm.beginTransaction();
                fragmentTransaction.remove(fm.findFragmentById(R.id.spell_creation));
                fragmentTransaction.add(R.id.map, new MapFragment());
                fragmentTransaction.add(R.id.status, new StatusBarFragment());
                fragmentTransaction.add(R.id.menu, new MenuFragment());
                fragmentTransaction.commit();
            }
        });
        Bitmap bm=Bitmap.createBitmap(width/5, width/5, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.YELLOW);
        Button element_view=(Button)getView().findViewById(R.id.element);
        Button mana_reservoir_view=(Button)getView().findViewById(R.id.mana_reservoir);
        Button type_view=(Button)getView().findViewById(R.id.type);
        Button mana_channel_view=(Button)getView().findViewById(R.id.mana_channel);
        Button form_view=(Button)getView().findViewById(R.id.form);
        RecyclerView comps=getView().findViewById(R.id.avaliable_components);
        comps.setLayoutManager(new LinearLayoutManager(getContext()));
        element_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter(MainActivity.elements));
            }
        });
        mana_reservoir_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter(MainActivity.manaReservoirs));
            }
        });
        mana_channel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter(MainActivity.manaChannels));
            }
        });
        type_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter(MainActivity.types));
            }
        });
        form_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comps.setAdapter(new SpellAdapter(MainActivity.forms));
            }
        });
    }

    class SpellAdapter<T extends Component> extends RecyclerView.Adapter<SpellAdapter.ViewHolder>{
        private final ArrayList<T> data=new ArrayList<>();

        public SpellAdapter(ArrayList<T> data) {
            for (int i=0;i<data.size();i++){
                if (data.get(i).isAvailable())
                    this.data.add(data.get(i));
            }
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
        public SpellCreationFragment.SpellAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.components_item, parent, false);
            return new SpellAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SpellCreationFragment.SpellAdapter.ViewHolder holder, int position) {
            Class r=data.get(position).getClass();
            holder.comp.setText(String.valueOf(data.get(position).getName()));
            if (r.equals(Element.class)) {
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm=getChildFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        element=(Element)data.get(position);
                        if (fm.findFragmentById(R.id.spell_characteristics)!=null)
                            fr.remove(fm.findFragmentById(R.id.spell_characteristics));
                        fr.add(R.id.spell_characteristics,
                                new SpellCharacteristicsFragment(
                                        new Spell(element,
                                                type,
                                                form,
                                                mana_channel,
                                                mana_reservoir,
                                                ((EditText)getView().findViewById(R.id.spell_name)).
                                                        getText().toString())));
                        fr.commit();
                    }
                });
            }
            else if (r.equals(Type.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm=getChildFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        type=(Type)data.get(position);
                        if (fm.findFragmentById(R.id.spell_characteristics)!=null)
                            fr.remove(fm.findFragmentById(R.id.spell_characteristics));
                        fr.add(R.id.spell_characteristics,
                                new SpellCharacteristicsFragment(
                                        new Spell(element,
                                                type,
                                                form,
                                                mana_channel,
                                                mana_reservoir,
                                                ((EditText)getView().findViewById(R.id.spell_name)).
                                                        getText().toString())));
                        fr.commit();
                    }
                });
            }
            else if (r.equals(ManaReservoir.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm=getChildFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        mana_reservoir=(ManaReservoir)data.get(position);
                        if (fm.findFragmentById(R.id.spell_characteristics)!=null)
                            fr.remove(fm.findFragmentById(R.id.spell_characteristics));
                        fr.add(R.id.spell_characteristics,
                                new SpellCharacteristicsFragment(
                                        new Spell(element,
                                                type,
                                                form,
                                                mana_channel,
                                                mana_reservoir,
                                                ((EditText)getView().findViewById(R.id.spell_name)).
                                                        getText().toString())));
                        fr.commit();
                    }
                });
            }
            else if (r.equals(ManaChannel.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm=getChildFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        mana_channel=(ManaChannel)data.get(position);
                        if (fm.findFragmentById(R.id.spell_characteristics)!=null)
                            fr.remove(fm.findFragmentById(R.id.spell_characteristics));
                        fr.add(R.id.spell_characteristics,
                                new SpellCharacteristicsFragment(
                                        new Spell(element,
                                                type,
                                                form,
                                                mana_channel,
                                                mana_reservoir,
                                                ((EditText)getView().findViewById(R.id.spell_name)).
                                                        getText().toString())));
                        fr.commit();
                    }
                });
            }
            else if (r.equals(Form.class)){
                holder.comp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm=getChildFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        form=(Form)data.get(position);
                        if (fm.findFragmentById(R.id.spell_characteristics)!=null)
                            fr.remove(fm.findFragmentById(R.id.spell_characteristics));
                        fr.add(R.id.spell_characteristics,
                                new SpellCharacteristicsFragment(
                                        new Spell(element,
                                                type,
                                                form,
                                                mana_channel,
                                                mana_reservoir,
                                                ((EditText)getView().findViewById(R.id.spell_name)).
                                                        getText().toString())));
                        fr.commit();
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