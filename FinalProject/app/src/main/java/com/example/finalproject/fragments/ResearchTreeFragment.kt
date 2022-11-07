package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R

class ResearchTreeFragment : Fragment(), View.OnClickListener {

    private lateinit var back: Button
    private lateinit var research: Button
    private lateinit var researchPointsAmount: TextView
    private lateinit var researchesList: RecyclerView
    private var chosenResearch = -1

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_research_tree, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        researchPointsAmount = requireView().findViewById(R.id.research_points_amount)
        back = requireView().findViewById(R.id.research_tree_back_button)
        research = requireView().findViewById(R.id.research_button)
        researchesList = requireView().findViewById(R.id.researches_list)
        researchesList.layoutManager = LinearLayoutManager(context)
        researchesList.adapter = ResearchListAdapter(assets.availableResearches)
        Log.d("RES", assets.availableResearches.toString())
        back.setOnClickListener(this)
        research.setOnClickListener(this)
        updateResearchPointsAmount()
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        if (p0 == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.research_tree)!!)
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        } else if (p0 == research) {
            if (chosenResearch >= 0) {
                val fragmentManager = childFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentManager.findFragmentById(R.id.research_charecteristics)
                    ?.let { it1 -> fragmentTransaction.remove(it1) }
                fragmentTransaction.commit()
                if (player.research(chosenResearch))
                    Toast.makeText(context, "Researched successfully", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "You can't research this now", Toast.LENGTH_SHORT)
                        .show()
                chosenResearch = -1
                updateResearchPointsAmount()
                researchesList.adapter = ResearchListAdapter(assets.availableResearches)
            } else
                Toast.makeText(context, "Choose research first", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * updates the research points amount view
     */
    @SuppressLint("SetTextI18n")
    private fun updateResearchPointsAmount() {
        researchPointsAmount.text = "You have ${player.researchPoints} research points"
    }

    private inner class ResearchListAdapter(val data: ArrayList<Int>) :
        RecyclerView.Adapter<ResearchListAdapter.ResearchListViewHolder>() {
        private inner class ResearchListViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.research_list_name)
        }

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchListViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.research_item, parent, false)
            return ResearchListViewHolder(view)
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ResearchListViewHolder, position: Int) {
            val currentResearch = assets.researches[data[position]]
            holder.name.text = "${currentResearch?.name}: ${currentResearch?.cost.toString()}"
            holder.name.setOnClickListener {
                val fragmentManager = childFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentManager.findFragmentById(R.id.research_charecteristics)
                    ?.let { it1 -> fragmentTransaction.remove(it1) }
                fragmentTransaction.add(
                    R.id.research_charecteristics,
                    ResearchCharacteristicsFragment(data[position])
                )
                fragmentTransaction.commit()
                chosenResearch = data[position]
            }
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size

    }
}