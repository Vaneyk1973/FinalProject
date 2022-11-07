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
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.R
import com.example.finalproject.service.classes.spell.Element
import com.example.finalproject.service.classes.spell.Form
import com.example.finalproject.service.classes.spell.ManaChannel
import com.example.finalproject.service.classes.spell.ManaReservoir
import com.example.finalproject.service.classes.spell.Spell
import com.example.finalproject.service.classes.spell.Type

class SpellCreationFragment : Fragment(), View.OnClickListener, TextView.OnEditorActionListener {

    private var element = assets.elements[0]
    private var type = assets.types[0]
    private var form = assets.forms[0]
    private var manaChannel = assets.manaChannels[0]
    private var manaReservoir = assets.manaReservoirs[0]
    private lateinit var nameView: EditText
    private lateinit var confirmSpell: Button
    private lateinit var back: Button
    private lateinit var elementView: Button
    private lateinit var manaReservoirView: Button
    private lateinit var typeView: Button
    private lateinit var manaChannelView: Button
    private lateinit var formView: Button
    private lateinit var comps: RecyclerView
    private var name: String = ""

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_spell_creation, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameView = requireView().findViewById(R.id.spell_name)
        confirmSpell = requireView().findViewById(R.id.confirm_creation)
        back = requireView().findViewById(R.id.spell_creation_back_button)
        elementView = requireView().findViewById(R.id.element)
        manaReservoirView = requireView().findViewById(R.id.mana_reservoir)
        typeView = requireView().findViewById(R.id.type)
        manaChannelView = requireView().findViewById(R.id.mana_channel)
        formView = requireView().findViewById(R.id.form)
        comps = requireView().findViewById(R.id.avaliable_components)
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
    private inner class SpellAdapter(dt: ArrayList<Int>) :
        RecyclerView.Adapter<SpellAdapter.ViewHolder>() {

        private val data: ArrayList<Int> = ArrayList()

        init {
            for (component in dt)
                if (assets.components[component]?.available == true)
                    data.add(component)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val comp: TextView = itemView.findViewById(R.id.comp)
        }

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.components_item, parent, false)
            return ViewHolder(view)
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.comp.text = assets.components[data[position]]?.name ?: ""
            holder.comp.setOnClickListener {
                when (data[position]) {
                    in assets.elements -> element = data[position]
                    in assets.types -> type = data[position]
                    in assets.forms -> form = data[position]
                    in assets.manaChannels -> manaChannel = data[position]
                    in assets.manaReservoirs -> manaReservoir = data[position]
                }
                val fm: FragmentManager = childFragmentManager
                val fr: FragmentTransaction = fm.beginTransaction()
                fm.findFragmentById(R.id.spell_characteristics)?.let { it1 -> fr.remove(it1) }
                fr.add(
                    R.id.spell_characteristics, SpellCharacteristicsFragment(
                        Spell(
                            assets.components[element] as Element,
                            assets.components[type] as Type,
                            assets.components[form] as Form,
                            assets.components[manaChannel] as ManaChannel,
                            assets.components[manaReservoir] as ManaReservoir,
                            (view!!.findViewById(R.id.spell_name) as EditText).text.toString()
                        )
                    )
                )
                fr.commit()
            }
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        when (p0) {
            confirmSpell -> {
                name = nameView.text.toString()
                MainActivity.player.spells.add(
                    Spell(
                        assets.components[element] as Element,
                        assets.components[type] as Type,
                        assets.components[form] as Form,
                        assets.components[manaChannel] as ManaChannel,
                        assets.components[manaReservoir] as ManaReservoir, name
                    )
                )
            }

            back -> {
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentManager.findFragmentById(R.id.spell_creation)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
                fragmentTransaction.add(R.id.status, StatusBarFragment())
                fragmentTransaction.add(R.id.menu, MenuFragment())
                fragmentTransaction.commit()
            }

            formView -> {
                comps.adapter = SpellAdapter(assets.forms)
            }

            typeView -> {
                comps.adapter = SpellAdapter(assets.types)
            }

            manaChannelView -> {
                comps.adapter = SpellAdapter(assets.manaChannels)
            }

            manaReservoirView -> {
                comps.adapter = SpellAdapter(assets.manaReservoirs)
            }

            elementView -> {
                comps.adapter = SpellAdapter(assets.elements)
            }
        }
    }

    /**
     * @param p0 the view that has been edited
     * sets the action on the event of editing a view
     */
    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p0 == nameView) {
            name = p0.text.toString()
            return true
        }
        return false
    }
}