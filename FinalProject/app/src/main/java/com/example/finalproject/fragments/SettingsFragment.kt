package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.finalproject.R

class SettingsFragment:Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var back:Button
    private lateinit var chatMode: SwitchCompat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatMode= requireView().findViewById(R.id.chat_mode_switch)
        chatMode.isChecked = MainActivity.player.chatMode
        chatMode.setOnCheckedChangeListener(this)
        back=requireView().findViewById(R.id.settings_back_button)
        back.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0==back) {
            val fragmentManager=parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.settings_menu, SettingsMenuFragment())
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.settings)!!)
            fragmentTransaction.commit()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if(p0==chatMode)
            MainActivity.player.chatMode = p1
    }
}