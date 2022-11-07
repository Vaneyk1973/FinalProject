package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.classes.spell.Spell

class SpellsFragment:Fragment(), View.OnClickListener {

    private lateinit var back:Button

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spells, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spells:RecyclerView =requireView().findViewById(R.id.spells_list)
        spells.layoutManager=LinearLayoutManager(context)
        spells.adapter= SpellsAdapter(MainActivity.player.spells)
        back=requireView().findViewById(R.id.spells_back_button)
        back.setOnClickListener(this)
    }

    private inner class SpellsAdapter(val spells: ArrayList<Spell>) :
        RecyclerView.Adapter<SpellsAdapter.SpellsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellsViewHolder {
            return SpellsViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.spells_item, parent, false) as View
            )
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        override fun onBindViewHolder(holder: SpellsViewHolder, position: Int) {
            holder.name.text = spells[position].name
            holder.name.setOnClickListener {
                val fm:FragmentManager=childFragmentManager
                val fr:FragmentTransaction=fm.beginTransaction()
                fm.findFragmentById(R.id.spells_char)?.let { it1 -> fr.remove(it1) }
                fr.add(R.id.spells_char, SpellCharacteristicsFragment(spells[position]))
                fr.commit()
            }
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int=spells.size

        inner class SpellsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name:TextView=itemView.findViewById(R.id.spell_in_list)
        }
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        if (p0==back){
            val fragmentManager:FragmentManager= parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.spells)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }
}