package com.example.finalproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.service.Research
import com.google.gson.Gson

class ResearchTreeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_research_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        research_hash_map = HashMap()
        research_hash_map1 = HashMap()
        research_hash_map!![MainActivity.researches[0]] =
            getView()!!.findViewById<View>(R.id.basic_spell_creation) as TextView
        research_hash_map!![MainActivity.researches[1]] =
            getView()!!.findViewById<View>(R.id.fire_mage) as TextView
        research_hash_map!![MainActivity.researches[2]] =
            getView()!!.findViewById<View>(R.id.water_mage) as TextView
        research_hash_map!![MainActivity.researches[3]] =
            getView()!!.findViewById<View>(R.id.earth_mage) as TextView
        research_hash_map!![MainActivity.researches[4]] =
            getView()!!.findViewById<View>(R.id.air_mage) as TextView
        research_hash_map1!![getView()!!.findViewById<View>(R.id.basic_spell_creation) as TextView] =
            MainActivity.researches[0]
        research_hash_map1!![getView()!!.findViewById<View>(R.id.fire_mage) as TextView] =
            MainActivity.researches[1]
        research_hash_map1!![getView()!!.findViewById<View>(R.id.water_mage) as TextView] =
            MainActivity.researches[2]
        research_hash_map1!![getView()!!.findViewById<View>(R.id.earth_mage) as TextView] =
            MainActivity.researches[3]
        research_hash_map1!![getView()!!.findViewById<View>(R.id.air_mage) as TextView] =
            MainActivity.researches[4]
        val rpa = getView()!!.findViewById<TextView>(R.id.research_points_amount)
        rpa.text = "Your research points amount: " +
                MainActivity.player.researchPoints + ""
        val n = View.OnClickListener { v ->
            if (MainActivity.player.research(research_hash_map1!![v]!!)) v.setBackgroundColor(Color.GREEN)
            rpa.text = "Your research points amount: " +
                    MainActivity.player.researchPoints + ""
            MainActivity.researchesJson.clear()
            for (i in MainActivity.researches.indices) MainActivity.researchesJson.add(
                Gson().toJson(
                    MainActivity.researches[i]
                )
            )
            MainActivity.elementsJson.clear()
            for (i in MainActivity.elements.indices) MainActivity.elementsJson.add(
                Gson().toJson(
                    MainActivity.elements[i]
                )
            )
            MainActivity.formsJson.clear()
            for (i in MainActivity.forms.indices) MainActivity.formsJson.add(
                Gson().toJson(
                    MainActivity.forms[i]
                )
            )
            MainActivity.typesJson.clear()
            for (i in MainActivity.types.indices) MainActivity.typesJson.add(
                Gson().toJson(
                    MainActivity.types[i]
                )
            )
            MainActivity.manaChannelsJson.clear()
            for (i in MainActivity.manaChannels.indices) MainActivity.manaChannelsJson.add(
                Gson().toJson(
                    MainActivity.manaChannels[i]
                )
            )
            MainActivity.manaReservoirsJson.clear()
            for (i in MainActivity.manaReservoirs.indices) MainActivity.manaReservoirsJson.add(
                Gson().toJson(
                    MainActivity.manaReservoirs[i]
                )
            )
        }
        for (i in MainActivity.researches.indices) {
            research_hash_map!![MainActivity.researches[i]]!!.text =
                """
${MainActivity.researches[i].name} : ${MainActivity.researches[i].cost}
"""
            research_hash_map!![MainActivity.researches[i]]!!
                .setOnClickListener(n)
            if (MainActivity.researches[i].researched) research_hash_map!![MainActivity.researches[i]]!!
                .setBackgroundColor(Color.GREEN)
        }
        val back = getView()!!.findViewById<Button>(R.id.research_tree_back_button)
        val fm = parentFragmentManager
        val k = getView()!!.findViewById<TableLayout>(R.id.research_table)
        back.setOnClickListener {
            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.remove(fm.findFragmentById(R.id.research_tree)!!)
            fragmentTransaction.add(
                R.id.map,
                MapFragment(MainActivity.player.mapNum)
            )
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }

    companion object {
        var research_hash_map: HashMap<Research, TextView>? = null
        var research_hash_map1: HashMap<TextView, Research>? = null
    }
}