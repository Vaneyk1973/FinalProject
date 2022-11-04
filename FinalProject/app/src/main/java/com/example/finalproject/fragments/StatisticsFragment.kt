package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.R

class StatisticsFragment : Fragment(), View.OnClickListener {

    private lateinit var itemsObtainedList: RecyclerView
    private lateinit var enemiesKilled: RecyclerView
    private lateinit var back: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsObtainedList = requireView().findViewById(R.id.items_obtained_list)
        enemiesKilled = requireView().findViewById(R.id.enemies_killed_list)
        back = requireView().findViewById(R.id.statistics_back_button)
        itemsObtainedList.layoutManager = LinearLayoutManager(context)
        enemiesKilled.layoutManager = LinearLayoutManager(context)
        itemsObtainedList.adapter = StatisticsAdapter(assets.itemsObtained.run {
            val data: ArrayList<Pair<Int, Int>> = ArrayList()
            for (i in this.keys) {
                data.add(Pair(i, this[i]!!))
            }
            data
        }, 0)
        enemiesKilled.adapter = StatisticsAdapter(assets.enemiesKilled.run {
            val data: ArrayList<Pair<Int, Int>> = ArrayList()
            for (i in this.keys) {
                data.add(Pair(i, this[i]!!))
            }
            data
        }, 1)
        back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.statistics)
                ?.let { fragmentTransaction.remove(it) }
            fragmentTransaction.add(R.id.settings_menu, SettingsMenuFragment())
            fragmentTransaction.commit()
        }
    }

    private inner class StatisticsAdapter(val data: ArrayList<Pair<Int, Int>>, val mode: Int) :
        RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

        private inner class StatisticsViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.statistics_item_element)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.statistics_item, parent, false)
            return StatisticsViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
            if (mode == 0) {
                holder.name.text =
                    "${assets.items[data[position].first]?.name}: ${data[position].second}"
            } else {
                holder.name.text =
                    "${assets.enemies[data[position].first]?.name}: ${data[position].second}"
            }
        }

        override fun getItemCount(): Int = data.size

    }
}