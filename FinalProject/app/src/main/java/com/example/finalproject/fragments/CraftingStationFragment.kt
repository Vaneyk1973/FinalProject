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

class CraftingStationFragment : Fragment() {
    private var chosenRecipe: Recipe? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crafting_station, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = getView()!!.findViewById<Button>(R.id.crafting_station_back_button)
        val craft = getView()!!.findViewById<Button>(R.id.craft_button)
        back.setOnClickListener {
            val fm = parentFragmentManager
            val fr = fm.beginTransaction()
            fr.remove(fm.findFragmentById(R.id.crafting_station)!!)
            fr.add(
                R.id.map,
                MapFragment(MainActivity.player.mapNum)
            )
            fr.add(R.id.status, StatusBarFragment())
            fr.add(R.id.menu, MenuFragment())
            fr.commit()
        }
        craft.setOnClickListener {
            if (chosenRecipe != null) {
                if (!MainActivity.player.craft(chosenRecipe!!)) Toast.makeText(
                    context,
                    "You don't have necessary ingredients",
                    Toast.LENGTH_SHORT
                ).show() else Toast.makeText(
                    context, "Crafted successfully", Toast.LENGTH_SHORT
                ).show()
            } else Toast.makeText(context, "Choose recipe first", Toast.LENGTH_SHORT).show()
        }
        val crafts: RecyclerView = getView()!!.findViewById(R.id.crafts)
        crafts.layoutManager = LinearLayoutManager(context)
        crafts.adapter = CraftingAdapter(MainActivity.recipes)
    }

    private inner class CraftingAdapter(data: ArrayList<Recipe>?) :
        RecyclerView.Adapter<CraftingAdapter.ViewHolder>() {
        private val data = ArrayList<Recipe>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(
                    context
                ).inflate(R.layout.crafting_recipe, parent, false)
            )
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            holder.name.text = ">" + data[position].product.first.name
            var a = ""
            for (i in 0 until data[position].getIngredients().size - 1) a += data[position].getIngredients()[i].first.name + " " + data[position].getIngredients()[i].second + ", "
            a += data[position].getIngredients()[data[position].getIngredients().size - 1].first.name + " " +
                    data[position].getIngredients()[data[position].getIngredients().size - 1].second
            holder.ingredients.text = a
            holder.name.setOnClickListener {
                val fm = childFragmentManager
                val fr = fm.beginTransaction()
                if (fm.findFragmentById(R.id.characteristics1) != null) fr.remove(
                    fm.findFragmentById(
                        R.id.characteristics1
                    )!!
                )
                fr.add(
                    R.id.characteristics1,
                    ItemCharacteristicsFragment(data[position].product.first)
                )
                fr.commit()
                chosenRecipe = data[position]
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView
            var ingredients: TextView

            init {
                name = itemView.findViewById(R.id.recipe_name)
                ingredients = itemView.findViewById(R.id.crafting_ingredients)
            }
        }

        init {
            this.data.addAll(data!!)
        }
    }
}