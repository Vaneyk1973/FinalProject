package com.example.finalproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.service.spell.Spell;

import org.jetbrains.annotations.NotNull;

public class SpellCharacteristicsFragment extends Fragment {
    private Spell spell;

    public SpellCharacteristicsFragment(Spell spell){
        this.spell=spell;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spell_characteristics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView img=getView().findViewById(R.id.element_picture);
        img.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.b[2][Math.abs(spell.getElement().getElement())],
                MainActivity.categoryImageWidth /3*2, MainActivity.categoryImageWidth /3*2, false));
        TextView name=getView().findViewById(R.id.spell_name_char),
                type=getView().findViewById(R.id.spell_type_char),
                damage=getView().findViewById(R.id.spell_damage_char),
                mana_consumption=getView().findViewById(R.id.spell_mana_consumption_char);
        name.setText(spell.getName());
        type.setText(spell.getType().getName()+"");
        damage.setText(spell.getDamage()+"");
        mana_consumption.setText(spell.getManaConsumption()+"");
    }
}