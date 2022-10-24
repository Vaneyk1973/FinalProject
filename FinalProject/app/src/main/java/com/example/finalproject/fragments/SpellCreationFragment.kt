package com.example.finalproject.fragments

import android.os.Bundle
import android.view.KeyEvent
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
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.Statistics.elements
import com.example.finalproject.service.Statistics.forms
import com.example.finalproject.service.Statistics.manaChannels
import com.example.finalproject.service.Statistics.manaReservoirs
import com.example.finalproject.service.Statistics.types
import com.example.finalproject.service.classes.spell.*

class SpellCreationFragment : Fragment(), View.OnClickListener, TextView.OnEditorActionListener {

    private var element = elements[0]
    private var type = types[0]
    private var form = forms[0]
    private var manaChannel = manaChannels[0]
    private var manaReservoir = manaReservoirs[0]
    private lateinit var nameView: EditText
    private lateinit var confirmSpell: Button
    private lateinit var back: Button
    private lateinit var elementView: Button
    private lateinit var manaReservoirView: Button
    private lateinit var typeView: Button
    private lateinit var manaChannelView: Button
    private lateinit var formView: Button
    private lateinit var comps: RecyclerView
    private var name: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spell_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameView= requireView().findViewById(R.id.spell_name)
        confirmSpell= requireView().findViewById(R.id.confirm_creation)
        back= requireView().findViewById(R.id.spell_creation_back_button)
        elementView= requireView().findViewById(R.id.element)
        manaReservoirView= requireView().findViewById(R.id.mana_reservoir)
        typeView= requireView().findViewById(R.id.type)
        manaChannelView= requireView().findViewById(R.id.mana_channel)
        formView= requireView().findViewById(R.id.form)
        comps= requireView().findViewById(R.id.avaliable_components)
        name = nameView.text.toString()
        nameView.setOnEditorActionListener(this)
        confirmSpell.setOnClickListener(this)
        back.setOnClickListener(this)
        comps.layoutManager = LinearLayoutManager(context)
        elementView.setOnClickListener(this)
        manaReservoirView.setOnClickListener(this)
        manaChannelView.setOnClickListener(this)
        typeView.setOnClickListener(this)
        formView.setOnClickListener(this)
    }

    private inner class SpellAdapter<T : Component>(dt: ArrayList<T>) :
        RecyclerView.Adapter<SpellAdapter<T>.ViewHolder>() {

        private val data: ArrayList<T> = ArrayList()

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
            holder.comp.text = data[position].name
            when (data[position].javaClass) {
                Element::javaClass -> element = data[position] as Element

                Type::javaClass -> type = data[position] as Type

                Form::javaClass -> form = data[position] as Form

                ManaChannel::javaClass -> manaChannel = data[position] as ManaChannel

                ManaReservoir::javaClass -> manaReservoir = data[position] as ManaReservoir
            }
            holder.comp.setOnClickListener {
                val fm: FragmentManager = childFragmentManager
                val fr: FragmentTransaction = fm.beginTransaction()
                fm.findFragmentById(R.id.spell_characteristics)?.let { it1 -> fr.remove(it1) }
                fr.add(
                    R.id.spell_characteristics, SpellCharacteristicsFragment(
                        Spell(
                            element, type, form, manaChannel, manaReservoir,
                            (view!!.findViewById(R.id.spell_name) as EditText).text.toString()
                        )
                    )
                )
                fr.commit()
            }
        }

        override fun getItemCount(): Int = data.size
    }

    override fun onClick(p0: View?) {
        if (p0==confirmSpell){
            name = nameView.text.toString()
            MainActivity.player.spells.add(
                Spell(element, type, form, manaChannel, manaReservoir, name!!)
            )
        } else if (p0==back){
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.spell_creation)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        } else if (p0==formView){
            comps.adapter = SpellAdapter(forms)
        } else if (p0==typeView) {
            comps.adapter = SpellAdapter(types)
        } else if (p0==manaChannelView) {
            comps.adapter = SpellAdapter(manaChannels)
        } else if (p0==manaReservoirView) {
            comps.adapter = SpellAdapter(manaReservoirs)
        } else if (p0==elementView) {
            comps.adapter = SpellAdapter(elements)
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p0 == nameView) {
            name = p0.text.toString()
            return true
        }
        return false
    }
}