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

    private var element = assets.elements[1024]
    private var type = assets.types[1034]
    private var form = assets.forms[1031]
    private var manaChannel = assets.manaChannels[1032]
    private var manaReservoir = assets.manaReservoirs[1033]
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
            for (component in dt) {
                if (assets.elements[component]?.available == true
                    || assets.types[component]?.available == true
                    || assets.forms[component]?.available == true
                    || assets.manaChannels[component]?.available == true
                    || assets.manaReservoirs[component]?.available == true
                )
                    data.add(component)
            }
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
            holder.comp.text =
                when (data[position]) {
                    in assets.elements -> assets.elements[data[position]]!!.name
                    in assets.types -> assets.types[data[position]]!!.name
                    in assets.forms -> assets.forms[data[position]]!!.name
                    in assets.manaChannels -> assets.manaChannels[data[position]]!!.name
                    in assets.manaReservoirs -> assets.manaReservoirs[data[position]]!!.name
                    else -> ""
                }
            holder.comp.setOnClickListener {
                when (data[position]) {
                    in assets.elements -> element = assets.elements[data[position]]
                    in assets.types -> type = assets.types[data[position]]
                    in assets.forms -> form = assets.forms[data[position]]
                    in assets.manaChannels -> manaChannel = assets.manaChannels[data[position]]
                    in assets.manaReservoirs -> manaReservoir =
                        assets.manaReservoirs[data[position]]
                }
                val fm: FragmentManager = childFragmentManager
                val fr: FragmentTransaction = fm.beginTransaction()
                fm.findFragmentById(R.id.spell_characteristics)?.let { it1 -> fr.remove(it1) }
                fr.add(
                    R.id.spell_characteristics, SpellCharacteristicsFragment(
                        Spell(
                            element!!, type!!, form!!, manaChannel!!, manaReservoir!!,
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
                        element!!, type!!, form!!, manaChannel!!, manaReservoir!!, name
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
                comps.adapter = SpellAdapter(assets.forms.keys.let {
                    val list = ArrayList<Int>()
                    for (i in it)
                        list.add(i)
                    list
                })
            }

            typeView -> {
                comps.adapter = SpellAdapter(assets.types.keys.let {
                    val list = ArrayList<Int>()
                    for (i in it)
                        list.add(i)
                    list
                })
            }

            manaChannelView -> {
                comps.adapter = SpellAdapter(assets.manaChannels.keys.let {
                    val list = ArrayList<Int>()
                    for (i in it)
                        list.add(i)
                    list
                })
            }

            manaReservoirView -> {
                comps.adapter = SpellAdapter(assets.manaReservoirs.keys.let {
                    val list = ArrayList<Int>()
                    for (i in it)
                        list.add(i)
                    list
                })
            }

            elementView -> {
                comps.adapter = SpellAdapter(assets.elements.keys.let {
                    val list = ArrayList<Int>()
                    for (i in it)
                        list.add(i)
                    list
                })
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