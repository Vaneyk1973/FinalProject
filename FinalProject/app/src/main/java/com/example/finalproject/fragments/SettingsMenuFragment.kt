package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R

class SettingsMenuFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val settings:TextView=requireView().findViewById(R.id.settings_button)
        val help:TextView=requireView().findViewById(R.id.help_button)
        val tasks:TextView=requireView().findViewById(R.id.tasks_button)
        val back:Button=requireView().findViewById(R.id.settings_menu_back_button)
        settings.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.remove(parentFragmentManager.findFragmentById(R.id.settings_menu)!!)
            fr.add(R.id.settings, SettingsFragment())
            fr.commit()
        }
        help.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.remove(parentFragmentManager.findFragmentById(R.id.settings_menu)!!)
            fr.add(R.id.tutorial, TutorialFragment())
            fr.commit()
        }
        tasks.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.remove(parentFragmentManager.findFragmentById(R.id.settings_menu)!!)
            fr.add(R.id.tasks, TaskManagerFragment())
            fr.commit()
        }
        back.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.remove(parentFragmentManager.findFragmentById(R.id.settings_menu)!!)
            fr.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fr.add(R.id.menu, MenuFragment())
            fr.add(R.id.status, StatusBarFragment())
            fr.commit()
        }
    }
}