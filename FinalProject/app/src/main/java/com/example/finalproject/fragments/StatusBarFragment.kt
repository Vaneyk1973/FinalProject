package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.finalproject.R
import kotlin.math.round

class StatusBarFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_status_bar, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expImg:ImageView=requireView().findViewById(R.id.exp_image)
        val goldImg:ImageView=requireView().findViewById(R.id.gold_image)
        val healthImg:ImageView=requireView().findViewById(R.id.health_image)
        val manaImg:ImageView=requireView().findViewById(R.id.mana_image)
        val avatar:ImageView=requireView().findViewById(R.id.avatar)
        val lvl:TextView=requireView().findViewById(R.id.level)
        val gold:TextView=requireView().findViewById(R.id.gold)
        val exp:TextView=requireView().findViewById(R.id.exp)
        val health:TextView=requireView().findViewById(R.id.health)
        val mana:TextView=requireView().findViewById(R.id.mana)
        val chat:Button=requireView().findViewById(R.id.chat_button)
        val fm:FragmentManager=parentFragmentManager
        chat.visibility=View.VISIBLE
        lvl.text=MainActivity.player.level.toString()
        gold.text=MainActivity.player.gold.toString()
        health.text="${round(MainActivity.player.health)}/${round(MainActivity.player.maxHealth)}"
        mana.text="${round(MainActivity.player.mana)}/${round(MainActivity.player.maxMana)}"
        exp.text="${round(MainActivity.player.experience)}/${round(MainActivity.player.experienceToNextLevelRequired)}"
        avatar.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.textures[5][5],
            MainActivity.avatarWidth, MainActivity.avatarWidth, false))
        goldImg.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.textures[3][5],
            MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false))
        healthImg.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.textures[2][5],
            MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false))
        manaImg.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.textures[3][1],
            MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false))
        expImg.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.textures[3][4],
            MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false))
        chat.setOnClickListener {
            if (isInternetAvailable()) {
                val fragmentTransaction = fm.beginTransaction()
                if (MainActivity.player.user.loggedIn) {
                    if (MainActivity.player.chatMode) {
                        fragmentTransaction.add(R.id.chat_mini, ChatMiniFragment())
                        chat.visibility = View.GONE
                    } else {
                        fragmentTransaction.add(R.id.chat, ChatFragment())
                        fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                        fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                        fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                    }
                } else {
                    fragmentTransaction.add(R.id.log_in, SignInFragment())
                    fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
                    fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
                }
                fragmentTransaction.commit()
            } else Toast.makeText(context, "Check your Internet connection", Toast.LENGTH_SHORT).show()
        }
        avatar.setOnClickListener {
            val fr = fm.beginTransaction()
            fr.remove(fm.findFragmentById(R.id.map)!!)
            fr.remove(fm.findFragmentById(R.id.menu)!!)
            fr.remove(fm.findFragmentById(R.id.status)!!)
            fr.add(R.id.settings_menu, SettingsMenuFragment())
            fr.commit()
        }
    }

    fun update(){

    }

    private fun isInternetAvailable():Boolean{
        val cm:ConnectivityManager= requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val an: NetworkInfo? =cm.activeNetworkInfo
        return an!=null&&
                an.isConnectedOrConnecting
    }
}