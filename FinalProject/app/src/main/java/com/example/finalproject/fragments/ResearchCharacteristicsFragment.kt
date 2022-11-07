package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.R

class ResearchCharacteristicsFragment(private val chosenResearch: Int) : Fragment() {

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_research_characteristics, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name: TextView = requireView().findViewById(R.id.research_name)
        val description: TextView = requireView().findViewById(R.id.research_description)
        val effects: TextView = requireView().findViewById(R.id.research_effects)
        name.text = assets.researches[chosenResearch]?.name
        description.text = assets.researches[chosenResearch]?.description
        effects.text = assets.researchEffects[assets.researches[chosenResearch]?.effect]?.toString()
    }
}