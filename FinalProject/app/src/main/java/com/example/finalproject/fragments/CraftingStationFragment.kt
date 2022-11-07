package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.R
import com.example.finalproject.service.classes.Recipe

class CraftingStationFragment : Fragment(), View.OnClickListener {

    private var chosenRecipe: Recipe? = null

    private lateinit var back:Button
    private lateinit var craft:Button

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crafting_station, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = requireView().findViewById(R.id.statistics_back_button)
        craft = requireView().findViewById(R.id.craft_button)
        back.setOnClickListener(this)
        craft.setOnClickListener(this)
        val crafts: RecyclerView = requireView().findViewById(R.id.crafts)
        crafts.layoutManager = LinearLayoutManager(context)
        crafts.adapter = CraftingAdapter(assets.recipes)
    }

    private inner class CraftingAdapter(val data: ArrayList<Recipe>) :
        RecyclerView.Adapter<CraftingAdapter.ViewHolder>() {

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.crafting_recipe, parent, false)
            )
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = ">${data[position].product.second.name}"
            var a = ""
            val size = data[position].ingredients.size
            for (i in 0 until size - 1)
                a += "${data[position].ingredients[i].second.name} ${data[position].ingredients[i].first},"
            a += "${data[position].ingredients[size - 1].second.name} ${data[position].ingredients[size - 1].first}"
            holder.ingredients.text = a
            holder.name.setOnClickListener {
                val fm = childFragmentManager
                val fr = fm.beginTransaction()
                fm.findFragmentById(R.id.characteristics1)?.let { it1 -> fr.remove(it1) }
                fr.add(
                    R.id.characteristics1,
                    ItemCharacteristicsFragment(data[position].product.second)
                )
                fr.commit()
                chosenRecipe = data[position]
            }
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.recipe_name)
            var ingredients: TextView = itemView.findViewById(R.id.crafting_ingredients)
        }
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        if (p0==back){
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.crafting_station)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        } else if (p0==craft){
            if (chosenRecipe != null) {
                if (!MainActivity.player.craft(chosenRecipe!!))
                    Toast.makeText(
                        context, "You don't have necessary ingredients",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(
                        context, "Crafted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
            } else Toast.makeText(context, "Choose recipe first", Toast.LENGTH_SHORT).show()
        }
    }
}