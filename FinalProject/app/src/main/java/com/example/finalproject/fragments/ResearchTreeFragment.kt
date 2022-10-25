package com.example.finalproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.R
import com.example.finalproject.service.classes.Research
import com.google.common.collect.HashBiMap

class ResearchTreeFragment : Fragment(), View.OnClickListener {

    private val researchHashMap: HashBiMap<Research, TextView> = HashBiMap.create()
    private lateinit var back: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_research_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val researchPointsAmount: TextView= requireView().findViewById(R.id.research_points_amount)
        var text="You have ${MainActivity.player.researchPoints} research points"
        researchPointsAmount.text=text
        initializeResearches(researchHashMap)
        back= requireView().findViewById(R.id.research_tree_back_button)
        for (i in assets.researches) {
            text="${i.name} : ${i.cost}"
            researchHashMap[i]!!.text = text
            researchHashMap[i]!!.setOnClickListener(this)
            if (i.researched)
                researchHashMap[i]!!.setBackgroundColor(Color.GREEN)
        }
        back.setOnClickListener(this)
    }

    private fun initializeResearches(researchHashMap:HashBiMap<Research, TextView>){
        researchHashMap[assets.researches[0]] =
            requireView().findViewById(R.id.basic_spell_creation) as TextView
        researchHashMap[assets.researches[1]] =
            requireView().findViewById(R.id.fire_mage) as TextView
        /*researchHashMap[assets.researches[2]] =
            requireView().findViewById(R.id.water_mage) as TextView
        researchHashMap[assets.researches[3]] =
            requireView().findViewById(R.id.earth_mage) as TextView
        researchHashMap[assets.researches[4]] =
            requireView().findViewById(R.id.air_mage) as TextView*/
    }

    override fun onClick(p0: View?) {
        if (p0 in researchHashMap.values){
            val researchPointsAmount: TextView= requireView().findViewById(R.id.research_points_amount)
            var text="You have ${MainActivity.player.researchPoints} research points"
            researchPointsAmount.text = text
            if (MainActivity.player.research(researchHashMap.inverse()[p0]!!)) {
                p0!!.setBackgroundColor(Color.GREEN)
                text="You have ${MainActivity.player.researchPoints} research points"
                researchPointsAmount.text =text
            }
        } else if (p0==back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.research_tree)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }
}