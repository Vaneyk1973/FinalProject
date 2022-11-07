package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.R

class TutorialFragment : Fragment(), OnClickListener {

    private lateinit var closeTutorial: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeTutorial = requireView().findViewById(R.id.close_tutorial_button)
        closeTutorial.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        MainActivity.showTutorial = false
        val fm = parentFragmentManager
        val fr = fm.beginTransaction()
        fr.add(R.id.status, StatusBarFragment())
        fr.add(R.id.menu, MenuFragment())
        fr.add(R.id.map, MapFragment())
        fr.remove(fm.findFragmentById(R.id.tutorial)!!)
        fr.commit()
    }
}