package com.example.finalproject.fragments

import android.annotation.SuppressLint
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
import com.google.common.collect.HashBiMap
import com.google.gson.Gson

class ResearchTreeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_research_tree, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val researchHashMap: HashBiMap<Research, TextView> = HashBiMap.create()
        researchHashMap[MainActivity.researches[0]] =
            requireView().findViewById(R.id.basic_spell_creation) as TextView
        researchHashMap[MainActivity.researches[1]] =
            requireView().findViewById(R.id.fire_mage) as TextView
        researchHashMap[MainActivity.researches[2]] =
            requireView().findViewById(R.id.water_mage) as TextView
        researchHashMap[MainActivity.researches[3]] =
            requireView().findViewById(R.id.earth_mage) as TextView
        researchHashMap[MainActivity.researches[4]] =
            requireView().findViewById(R.id.air_mage) as TextView
        val researchPointsAmount: TextView = requireView().findViewById(R.id.research_points_amount)
        researchPointsAmount.text = "You have ${MainActivity.player.researchPoints} research points"
        val clickListener = View.OnClickListener { v ->
            if (MainActivity.player.research(researchHashMap.inverse()[v]!!)) {
                v.setBackgroundColor(Color.GREEN)
                updateJsons()
                researchPointsAmount.text =
                    "You have ${MainActivity.player.researchPoints} research points"
            }
        }
        for (i in MainActivity.researches) {
            researchHashMap[i]!!.text = "${i.name} : ${i.cost}"
            researchHashMap[i]!!.setOnClickListener(clickListener)
            if (i.researched)
                researchHashMap[i]!!.setBackgroundColor(Color.GREEN)
        }
        val back: Button = requireView().findViewById(R.id.research_tree_back_button)
        val fm = parentFragmentManager
        back.setOnClickListener {
            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.remove(fm.findFragmentById(R.id.research_tree)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }

    private fun updateJsons() {
        MainActivity.researchesJson.clear()
        for (i in MainActivity.researches)
            MainActivity.researchesJson.add(
                Gson().toJson(i))
        MainActivity.elementsJson.clear()
        for (i in MainActivity.elements)
            MainActivity.elementsJson.add(
                Gson().toJson(i))
        MainActivity.formsJson.clear()
        for (i in MainActivity.forms)
            MainActivity.formsJson.add(
                Gson().toJson(i))
        MainActivity.typesJson.clear()
        for (i in MainActivity.types)
            MainActivity.typesJson.add(
                Gson().toJson(i))
        MainActivity.manaChannelsJson.clear()
        for (i in MainActivity.manaChannels)
            MainActivity.manaChannelsJson.add(
                Gson().toJson(i))
        MainActivity.manaReservoirsJson.clear()
        for (i in MainActivity.manaReservoirs)
            MainActivity.manaReservoirsJson.add(
                Gson().toJson(i))
    }
}