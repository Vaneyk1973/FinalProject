package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import kotlin.math.floor

class CharacteristicsFragment : Fragment(), View.OnClickListener {

    private lateinit var damageList: RecyclerView
    private lateinit var resistancesList: RecyclerView
    private lateinit var back: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_characteristics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        damageList = requireView().findViewById(R.id.damage_characteristics)
        resistancesList = requireView().findViewById(R.id.resistances_characteristics)
        back = requireView().findViewById(R.id.settings_characteristics_back_button)
        damageList.layoutManager = LinearLayoutManager(context)
        resistancesList.layoutManager = LinearLayoutManager(context)
        damageList.adapter = CharacteristicsAdapter(MainActivity.player.damage.dmg, 0)
        resistancesList.adapter =
            CharacteristicsAdapter(MainActivity.player.resistances.resistances, 1)
        back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.characteristics)
                ?.let { fragmentTransaction.remove(it) }
            fragmentTransaction.add(R.id.settings_menu, SettingsMenuFragment())
            fragmentTransaction.commit()
        }
    }

    private inner class CharacteristicsAdapter(val data: ArrayList<Double>, val mode: Int) :
        RecyclerView.Adapter<CharacteristicsAdapter.CharacteristicsViewHolder>() {
        private val names = arrayListOf(
            "Physical",
            "Pure mana",
            "Fire",
            "Water",
            "Air",
            "Earth",
            "Death",
            "Life"
        )

        private inner class CharacteristicsViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val view: TextView = itemView.findViewById(R.id.characteristic)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CharacteristicsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.characteristics_item, parent, false)
            return CharacteristicsViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CharacteristicsViewHolder, position: Int) {
            if (mode == 0) {
                holder.view.text = "${names[position]}: ${data[position]}"
            } else {
                holder.view.text = "${names[position]}: ${floor(data[position] * 10000)/100}%"
            }
        }

    }
}