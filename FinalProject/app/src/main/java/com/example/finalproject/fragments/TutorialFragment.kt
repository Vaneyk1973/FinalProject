package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.R

class TutorialFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var sw: SwitchCompat
    private lateinit var closeTutorial:Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sw = requireView().findViewById(R.id.show_tutorial)
        sw.setOnCheckedChangeListener(this)
        sw.isChecked = true
        closeTutorial= requireView().findViewById(R.id.close_tutorial_button)
        closeTutorial.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        MainActivity.showTutorial = sw.isChecked
        val fm = parentFragmentManager
        val fr = fm.beginTransaction()
        fr.add(R.id.status, StatusBarFragment())
        fr.add(R.id.menu, MenuFragment())
        fr.add(R.id.map, MapFragment())
        fr.remove(fm.findFragmentById(R.id.tutorial)!!)
        fr.commit()
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        MainActivity.showTutorial = (p0 as SwitchCompat).isChecked
    }
}