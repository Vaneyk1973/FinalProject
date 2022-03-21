package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.service.spell.*

class SpellCreationFragment: Fragment() {
    private var element = MainActivity.elements[0]
    private var type = MainActivity.types[0]
    private var form = MainActivity.forms[0]
    private var manaChannel = MainActivity.manaChannels[0]
    private var manaReservoir = MainActivity.manaReservoirs[0]
    private var name: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spell_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameView:EditText = requireView().findViewById(R.id.spell_name)
        val confirmSpell:Button = requireView().findViewById(R.id.confirm_creation)
        val back:Button = requireView().findViewById(R.id.spell_creation_back_button)
        val elementView:Button = requireView().findViewById(R.id.element)
        val manaReservoirView:Button = requireView().findViewById(R.id.mana_reservoir)
        val typeView:Button = requireView().findViewById(R.id.type)
        val manaChannelView:Button = requireView().findViewById(R.id.mana_channel)
        val formView:Button = requireView().findViewById(R.id.form)
        val comps: RecyclerView = requireView().findViewById(R.id.avaliable_components)
        name = nameView.text.toString()
        nameView.setOnEditorActionListener { v, _, _ ->
            name = v.text.toString()
            false
        }
        confirmSpell.setOnClickListener {
            name = nameView.text.toString()
            MainActivity.player.spells.add(
                Spell(element, type, form, manaChannel, manaReservoir, name!!))
        }
        back.setOnClickListener {
            val fm = parentFragmentManager
            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.remove(fm.findFragmentById(R.id.spell_creation)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
        comps.layoutManager = LinearLayoutManager(context)
        elementView.setOnClickListener {
            comps.adapter = SpellAdapter(MainActivity.elements)
        }
        manaReservoirView.setOnClickListener {
            comps.adapter = SpellAdapter(MainActivity.manaReservoirs)
        }
        manaChannelView.setOnClickListener {
            comps.adapter = SpellAdapter(MainActivity.manaChannels)
        }
        typeView.setOnClickListener {
            comps.adapter = SpellAdapter(MainActivity.types)
        }
        formView.setOnClickListener {
            comps.adapter = SpellAdapter(MainActivity.forms)
        }
    }

    inner class SpellAdapter<T : Component>(dt:ArrayList<T>) : RecyclerView.Adapter<SpellAdapter<T>.ViewHolder>() {
        private val data:ArrayList<T> = ArrayList()
        init {
            for (i in dt)
                if (i.available)
                    data.add(i)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val comp: TextView = itemView.findViewById(R.id.comp)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.components_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.comp.text=data[position].name
            when (data[position].javaClass){
                Element::javaClass->element=data[position] as Element

                Type::javaClass->type=data[position] as Type

                Form::javaClass->form=data[position] as Form

                ManaChannel::javaClass->manaChannel=data[position] as ManaChannel

                ManaReservoir::javaClass->manaReservoir=data[position] as ManaReservoir
            }
            holder.comp.setOnClickListener {
                val fm:FragmentManager=childFragmentManager
                val fr:FragmentTransaction=fm.beginTransaction()
                fm.findFragmentById(R.id.spell_characteristics)?.let { it1 -> fr.remove(it1) }
                fr.add(R.id.spell_characteristics, SpellCharacteristicsFragment(
                    Spell(element, type, form, manaChannel, manaReservoir,
                        (view!!.findViewById(R.id.spell_name) as EditText).text.toString()
                    )))
                fr.commit()
            }
        }

        override fun getItemCount(): Int=data.size
    }
}