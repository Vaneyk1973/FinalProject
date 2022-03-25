package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.finalproject.R

class TutorialFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sw: Switch = requireView().findViewById(R.id.show_tutorial)
        Log.d("Avatar", MainActivity.player.getTileTexture().toString())
        sw.setOnCheckedChangeListener { _, _ ->
            MainActivity.showTutorial = sw.isChecked
        }
        sw.isChecked = true
        val closeTutorial = requireView().findViewById<Button>(R.id.close_tutorial_button)
        closeTutorial.setOnClickListener {
            MainActivity.showTutorial = sw.isChecked
            val fm = parentFragmentManager
            val fr = fm.beginTransaction()
            fr.add(R.id.status, StatusBarFragment())
            fr.add(R.id.menu, MenuFragment())
            fr.add(R.id.map, MapFragment())
            fr.remove(fm.findFragmentById(R.id.tutorial)!!)
            fr.commit()
        }
    }
}