package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.finalproject.R

class SettingsFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatMode: SwitchCompat = requireView().findViewById(R.id.chat_mode_switch)
        chatMode.isChecked = MainActivity.player.chatMode
        chatMode.setOnCheckedChangeListener { buttonView, isChecked ->
            MainActivity.player.chatMode = isChecked
        }
        val back:Button=requireView().findViewById(R.id.settings_back_button)
        back.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.add(R.id.settings_menu, SettingsMenuFragment())
            fr.remove(parentFragmentManager.findFragmentById(R.id.settings)!!)
            fr.commit() }
    }
}