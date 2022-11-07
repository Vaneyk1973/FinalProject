package com.example.finalproject.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.classes.spell.Spell

class SpellCharacteristicsFragment(val spell: Spell):Fragment() {

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spell_characteristics, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img:ImageView=requireView().findViewById(R.id.element_picture)
        img.setImageBitmap(Bitmap.createScaledBitmap(
            MainActivity.textures[2][spell.getElement().element-1],
            MainActivity.categoryImageWidth/3*2, MainActivity.categoryImageWidth/3*2, false))
        val name:TextView=requireView().findViewById(R.id.spell_name_char)
        val type:TextView=requireView().findViewById(R.id.spell_type_char)
        val damage:TextView=requireView().findViewById(R.id.spell_damage_char)
        val manaConsumption:TextView=requireView().findViewById(R.id.spell_mana_consumption_char)
        name.text=spell.name
        type.text=spell.getType().name
        damage.text=spell.damage.toString()
        manaConsumption.text=spell.manaConsumption.toString()
    }
}