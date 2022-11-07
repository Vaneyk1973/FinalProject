package com.example.finalproject.fragments

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.round

class StatusBarFragment : Fragment(), View.OnClickListener {

    private lateinit var lvl: TextView
    private lateinit var gold: TextView
    private lateinit var exp: TextView
    private lateinit var health: TextView
    private lateinit var mana: TextView
    private lateinit var chat: FloatingActionButton
    private lateinit var avatar: ImageView

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_status_bar, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val expImg: ImageView = requireView().findViewById(R.id.exp_image)
        val goldImg: ImageView = requireView().findViewById(R.id.gold_image)
        val healthImg: ImageView = requireView().findViewById(R.id.health_image)
        val manaImg: ImageView = requireView().findViewById(R.id.mana_image)
        avatar = requireView().findViewById(R.id.avatar)
        lvl = requireView().findViewById(R.id.level)
        gold = requireView().findViewById(R.id.gold)
        exp = requireView().findViewById(R.id.exp)
        health = requireView().findViewById(R.id.health)
        mana = requireView().findViewById(R.id.mana)
        chat = requireView().findViewById(R.id.chat_button)
        chat.visibility = View.VISIBLE
        avatar.setImageBitmap(
            Bitmap.createScaledBitmap(
                MainActivity.textures[5][5],
                MainActivity.avatarWidth, MainActivity.avatarWidth, false
            )
        )
        goldImg.setImageBitmap(
            Bitmap.createScaledBitmap(
                MainActivity.textures[3][5],
                MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false
            )
        )
        healthImg.setImageBitmap(
            Bitmap.createScaledBitmap(
                MainActivity.textures[2][6],
                MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false
            )
        )
        manaImg.setImageBitmap(
            Bitmap.createScaledBitmap(
                MainActivity.textures[3][1],
                MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false
            )
        )
        expImg.setImageBitmap(
            Bitmap.createScaledBitmap(
                MainActivity.textures[3][4],
                MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false
            )
        )
        chat.setOnClickListener(this)
        avatar.setOnClickListener(this)
        update()
    }

    /**
     * updates the information presented in the views
     */
    fun update() {
        lvl.text = MainActivity.player.level.toString()
        gold.text = MainActivity.player.gold.toString()
        var txt = "${round(MainActivity.player.health)}/${round(MainActivity.player.maxHealth)}"
        health.text = txt
        txt = "${round(MainActivity.player.mana)}/${round(MainActivity.player.maxMana)}"
        mana.text = txt
        txt =
            "${MainActivity.player.experience}/${MainActivity.player.experienceToTheNextLevelRequired}"
        exp.text = txt
    }

    /**
     * @return true if the network is available, false otherwise
     */
    private fun isInternetAvailable(): Boolean {
        val cm: ConnectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val an: NetworkInfo? = cm.activeNetworkInfo
        return an != null &&
                an.isConnectedOrConnecting
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        val fm: FragmentManager = parentFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        if (p0 == chat) {
            if (isInternetAvailable()) {
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
            } else Toast.makeText(context, "Check your Internet connection", Toast.LENGTH_SHORT)
                .show()
        } else if (p0 == avatar) {
            fragmentTransaction.remove(fm.findFragmentById(R.id.map)!!)
            fragmentTransaction.remove(fm.findFragmentById(R.id.menu)!!)
            fragmentTransaction.remove(fm.findFragmentById(R.id.status)!!)
            fragmentTransaction.add(R.id.settings_menu, SettingsMenuFragment())
        }
        fragmentTransaction.commit()
    }
}