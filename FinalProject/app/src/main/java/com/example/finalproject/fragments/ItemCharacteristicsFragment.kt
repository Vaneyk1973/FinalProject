package com.example.finalproject.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.items.Item
import kotlin.math.cos

class ItemCharacteristicsFragment(val item: Item, private val mode: Boolean = true) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_characteristics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img: ImageView = requireView().findViewById(R.id.category_image)
        img.setImageBitmap(
            Bitmap.createScaledBitmap(
                MainActivity.textures[6][item.category],
                MainActivity.categoryImageWidth, MainActivity.categoryImageWidth, false
            )
        )
        val name: TextView = requireView().findViewById(R.id.name_field)
        val category: TextView = requireView().findViewById(R.id.category_field)
        val cost: TextView = requireView().findViewById(R.id.cost_field)
        name.text = item.name
        category.text = item.category.toString()
        if (mode)
            cost.text = item.costSell.toString()
        else
            cost.text = item.costBuy.toString()
    }
}