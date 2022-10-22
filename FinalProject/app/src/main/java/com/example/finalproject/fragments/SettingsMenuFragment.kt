package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R

class SettingsMenuFragment : Fragment(), View.OnClickListener {

    private lateinit var settings: TextView
    private lateinit var help: TextView
    private lateinit var tasks: TextView
    private lateinit var back: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings= requireView().findViewById(R.id.settings_button)
        tasks= requireView().findViewById(R.id.tasks_button)
        help= requireView().findViewById(R.id.help_button)
        back= requireView().findViewById(R.id.settings_menu_back_button)
        settings.setOnClickListener(this)
        help.setOnClickListener(this)
        tasks.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val fragmentManager=parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (p0) {
            back -> {
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.settings_menu)!!)
                fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
                fragmentTransaction.add(R.id.menu, MenuFragment())
                fragmentTransaction.add(R.id.status, StatusBarFragment())
            }
            settings -> {
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.settings_menu)!!)
                fragmentTransaction.add(R.id.settings, SettingsFragment())
            }
            help -> {
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.settings_menu)!!)
                fragmentTransaction.add(R.id.tutorial, TutorialFragment())
            }
            tasks -> {
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.settings_menu)!!)
                fragmentTransaction.add(R.id.tasks, TaskManagerFragment())
            }
        }
        fragmentTransaction.commit()
    }
}