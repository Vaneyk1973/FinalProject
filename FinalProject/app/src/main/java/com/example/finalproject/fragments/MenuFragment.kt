package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalproject.R

class MenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cntrlpanel:Array<ImageView> = arrayOf(
            requireView().findViewById(R.id.inventory_button),
            requireView().findViewById(R.id.spellCreation_button),
            requireView().findViewById(R.id.spells_button),
            requireView().findViewById(R.id.research_tree_button)
        )
        val fm = parentFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        for (i in cntrlpanel.indices)
            cntrlpanel[i].setImageBitmap(MainActivity.menu[i])
        cntrlpanel[0].setOnClickListener{
            fragmentTransaction.add(R.id.inventory, InventoryFragment())
            fm.findFragmentById(R.id.map)?.let { it1 -> fragmentTransaction.remove(it1) }
            fm.findFragmentById(R.id.chat)?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
            fragmentTransaction.commit()
        }
        cntrlpanel[1].setOnClickListener{
            if (MainActivity.researches[0].researched) {
                fragmentTransaction.add(R.id.spell_creation, SpellCreationFragment())
                fm.findFragmentById(R.id.map)?.let { it1 -> fragmentTransaction.remove(it1) }
                fm.findFragmentById(R.id.chat)?.let { it1 -> fragmentTransaction.remove(it1) }
                fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                fragmentTransaction.commit()
            } else
                Toast.makeText(context, "You don't know how to make spells yet", Toast.LENGTH_SHORT).show()
        }
        cntrlpanel[2].setOnClickListener{
            if (MainActivity.researches[0].researched) {
                if (MainActivity.player.spells.isEmpty())
                    Toast.makeText(context, "You don't have any spells yet", Toast.LENGTH_SHORT).show()
                else {
                    fragmentTransaction.add(R.id.spells, SpellsFragment())
                    fm.findFragmentById(R.id.map)?.let { it1 -> fragmentTransaction.remove(it1) }
                    fm.findFragmentById(R.id.chat)?.let { it1 -> fragmentTransaction.remove(it1) }
                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                    fragmentTransaction.commit()
                }
            } else
                Toast.makeText(context, "You don't know how to make spells yet", Toast.LENGTH_SHORT).show()
        }
        cntrlpanel[3].setOnClickListener{
            fragmentTransaction.add(R.id.research_tree, ResearchTreeFragment())
            fm.findFragmentById(R.id.map)?.let { it1 -> fragmentTransaction.remove(it1) }
            fm.findFragmentById(R.id.chat)?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
            fragmentTransaction.commit()
        }
    }
}