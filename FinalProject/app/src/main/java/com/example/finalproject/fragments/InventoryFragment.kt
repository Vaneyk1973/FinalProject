package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.items.Item

class InventoryFragment : Fragment() {
    private var noItems: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noItems = requireView().findViewById(R.id.textView12)
        val inventory:RecyclerView = requireView().findViewById(R.id.list)
        val categories = ArrayList<ImageView>()
        val back:Button = requireView().findViewById(R.id.inventory_back_button)
        val clickListener = View.OnClickListener { v->
            inventory.adapter = InventoryAdapter(MainActivity.player.inventory, categories.indexOf(v))
        }
        var bm:Bitmap
        categories.add(requireView().findViewById(R.id.armor_weapons))
        categories.add(requireView().findViewById(R.id.potions_food))
        categories.add(requireView().findViewById(R.id.recourses))
        categories.add(requireView().findViewById(R.id.other))
        for (i in 0..3){
            bm = Bitmap.createScaledBitmap(
                MainActivity.textures[6][i],
                MainActivity.categoryImageWidth,
                MainActivity.categoryImageWidth,
                false
            )
            categories[i].setImageBitmap(Bitmap.createBitmap(bm))
            categories[i].setOnClickListener(clickListener)
        }
        inventory.adapter = InventoryAdapter(MainActivity.player.inventory)
        inventory.layoutManager = LinearLayoutManager(context)
        back.setOnClickListener {
            val fm = parentFragmentManager
            val fragmentTransaction = fm.beginTransaction()
            fm.findFragmentById(R.id.inventory)?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }

    private inner class InventoryAdapter(val data: ArrayList<Pair<Item, Int>>, val category:Int=0)
        :RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = ">${data[position].first.name}:${data[position].second}"
            holder.name.setOnClickListener {
                if (data[position].first.category == 0) {
                    MainActivity.player.equip(data[position].first)
                }
                val fm = childFragmentManager
                val fr = fm.beginTransaction()
                fm.findFragmentById(R.id.characteristics)?.let { it1 -> fr.remove(it1) }
                fr.add(R.id.characteristics, ItemCharacteristicsFragment(data[position].first))
                fr.commit()
            }
        }

        override fun getItemCount(): Int {
            return if (category == -1) {
                if (data.size == 0) noItems!!.visibility = View.VISIBLE else noItems!!.visibility =
                    View.GONE
                data.size
            } else {
                val data1 = ArrayList<Pair<Item, Int>>()
                for (i in data.indices) {
                    if (data[i].first.category == category) data1.add(data[i])
                }
                data.clear()
                data.addAll(data1)
                if (data.size == 0) noItems!!.visibility = View.VISIBLE else noItems!!.visibility =
                    View.GONE
                data1.size
            }
        }
    }
}