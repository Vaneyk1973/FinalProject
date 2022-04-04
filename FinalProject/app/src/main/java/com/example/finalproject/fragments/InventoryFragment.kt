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

class InventoryFragment : Fragment(), View.OnClickListener {

    private var noItems: TextView? = null
    private val categories = ArrayList<ImageView>()

    private lateinit var back: Button
    private lateinit var inventory: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noItems = requireView().findViewById(R.id.textView12)
        inventory = requireView().findViewById(R.id.list)
        back = requireView().findViewById(R.id.inventory_back_button)
        categories.add(requireView().findViewById(R.id.armor_weapons))
        categories.add(requireView().findViewById(R.id.potions_food))
        categories.add(requireView().findViewById(R.id.recourses))
        categories.add(requireView().findViewById(R.id.other))
        for (i in 0..3) {
            val bm: Bitmap = Bitmap.createScaledBitmap(
                MainActivity.textures[6][i],
                MainActivity.categoryImageWidth,
                MainActivity.categoryImageWidth,
                false
            )
            categories[i].setImageBitmap(Bitmap.createBitmap(bm))
            categories[i].setOnClickListener(this)
        }
        inventory.adapter = InventoryAdapter(MainActivity.player.inventory)
        inventory.layoutManager = LinearLayoutManager(context)
        back.setOnClickListener(this)
    }

    private inner class InventoryAdapter(
        val data: ArrayList<Pair<Item, Int>>,
        val category: Int = 0
    ) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

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

    override fun onClick(p0: View?) {
        if (p0 is ImageView) {
            inventory.adapter =
                InventoryAdapter(MainActivity.player.inventory, categories.indexOf(p0))
        } else if (p0 == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.inventory)
                ?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }
}