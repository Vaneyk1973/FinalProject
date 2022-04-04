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
import com.example.finalproject.R
import com.example.finalproject.items.Recipe

class CraftingStationFragment : Fragment(), View.OnClickListener {

    private var chosenRecipe: Recipe? = null

    private lateinit var back:Button
    private lateinit var craft:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crafting_station, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = requireView().findViewById(R.id.crafting_station_back_button)
        craft = requireView().findViewById(R.id.craft_button)
        back.setOnClickListener(this)
        craft.setOnClickListener(this)
        val crafts: RecyclerView = requireView().findViewById(R.id.crafts)
        crafts.layoutManager = LinearLayoutManager(context)
        crafts.adapter = CraftingAdapter(MainActivity.recipes)
    }

    private inner class CraftingAdapter(val data: ArrayList<Recipe>) :
        RecyclerView.Adapter<CraftingAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.crafting_recipe, parent, false)
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = ">${data[position].product.first.name}"
            var a = ""
            val size = data[position].getIngredients().size
            for (i in 0 until size - 1)
                a += "${data[position].getIngredients()[i].first.name} ${data[position].getIngredients()[i].second},"
            a += "${data[position].getIngredients()[size - 1].first.name} ${data[position].getIngredients()[size - 1].second}"
            holder.ingredients.text = a
            holder.name.setOnClickListener {
                val fm = childFragmentManager
                val fr = fm.beginTransaction()
                fm.findFragmentById(R.id.characteristics1)?.let { it1 -> fr.remove(it1) }
                fr.add(
                    R.id.characteristics1,
                    ItemCharacteristicsFragment(data[position].product.first)
                )
                fr.commit()
                chosenRecipe = data[position]
            }
        }

        override fun getItemCount(): Int = data.size

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.recipe_name)
            var ingredients: TextView = itemView.findViewById(R.id.crafting_ingredients)
        }
    }

    override fun onClick(p0: View?) {
        if (p0==back){
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.crafting_station)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
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