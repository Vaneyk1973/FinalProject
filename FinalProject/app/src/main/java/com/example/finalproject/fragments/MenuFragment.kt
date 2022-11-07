package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.R

private const val SPELL_CREATION_RESEARCH_ID = 1280
private const val SPELL_USAGE_RESEARCH_ID=1282
class MenuFragment : Fragment(), View.OnClickListener {

    private lateinit var controlPanel: Array<ImageView>

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlPanel = arrayOf(
            requireView().findViewById(R.id.inventory_button),
            requireView().findViewById(R.id.spellCreation_button),
            requireView().findViewById(R.id.spells_button),
            requireView().findViewById(R.id.research_tree_button)
        )

        for (i in controlPanel.indices) {
            controlPanel[i].setImageBitmap(MainActivity.menu[i])
            controlPanel[i].setOnClickListener(this)
        }
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (p0 == controlPanel[0]) {
            fragmentTransaction.add(R.id.inventory, InventoryFragment())
            fragmentManager.findFragmentById(R.id.map)
                ?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentManager.findFragmentById(R.id.chat)
                ?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.status)!!)
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.menu)!!)
            fragmentTransaction.commit()
        } else if (p0 == controlPanel[1]) {
            if (assets.researches[SPELL_CREATION_RESEARCH_ID]?.researched == true) {
                fragmentTransaction.add(R.id.spell_creation, SpellCreationFragment())
                fragmentManager.findFragmentById(R.id.map)
                    ?.let { it1 -> fragmentTransaction.remove(it1) }
                fragmentManager.findFragmentById(R.id.chat)
                    ?.let { it1 -> fragmentTransaction.remove(it1) }
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.status)!!)
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.menu)!!)
                fragmentTransaction.commit()
            } else
                Toast.makeText(context, "You don't know how to make spells yet", Toast.LENGTH_SHORT)
                    .show()
        } else if (p0 == controlPanel[2]) {
            if (assets.researches[SPELL_USAGE_RESEARCH_ID]?.researched == true) {
                if (MainActivity.player.spells.isEmpty())
                    Toast.makeText(context, "You don't have any spells yet", Toast.LENGTH_SHORT)
                        .show()
                else {
                    fragmentTransaction.add(R.id.spells, SpellsFragment())
                    fragmentManager.findFragmentById(R.id.map)
                        ?.let { it1 -> fragmentTransaction.remove(it1) }
                    fragmentManager.findFragmentById(R.id.chat)
                        ?.let { it1 -> fragmentTransaction.remove(it1) }
                    fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.status)!!)
                    fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.menu)!!)
                    fragmentTransaction.commit()
                }
            } else
                Toast.makeText(context, "You don't know how to use spells yet", Toast.LENGTH_SHORT)
                    .show()
        } else if (p0 == controlPanel[3]) {
            fragmentTransaction.add(R.id.research_tree, ResearchTreeFragment())
            fragmentManager.findFragmentById(R.id.map)
                ?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentManager.findFragmentById(R.id.chat)
                ?.let { it1 -> fragmentTransaction.remove(it1) }
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.status)!!)
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.menu)!!)
            fragmentTransaction.commit()
        }
    }
}