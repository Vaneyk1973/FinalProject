package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.classes.entities.Player
import kotlinx.serialization.json.Json
import kotlin.concurrent.thread

class SettingsMenuFragment : Fragment(), View.OnClickListener {

    private lateinit var settings: TextView
    private lateinit var characteristics: TextView
    private lateinit var statistics: TextView
    private lateinit var help: TextView
    private lateinit var tasks: TextView
    private lateinit var back: Button
    private lateinit var save: Button

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_menu, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings = requireView().findViewById(R.id.settings_button)
        characteristics = requireView().findViewById(R.id.characteristics_button)
        statistics = requireView().findViewById(R.id.statistics_button)
        tasks = requireView().findViewById(R.id.tasks_button)
        help = requireView().findViewById(R.id.help_button)
        back = requireView().findViewById(R.id.settings_menu_back_button)
        save = requireView().findViewById(R.id.save_button)
        settings.setOnClickListener(this)
        characteristics.setOnClickListener(this)
        statistics.setOnClickListener(this)
        help.setOnClickListener(this)
        tasks.setOnClickListener(this)
        back.setOnClickListener(this)
        save.setOnClickListener(this)
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (p0) {
            back -> {
                fragmentManager.findFragmentById(R.id.settings_menu)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
                fragmentTransaction.add(R.id.menu, MenuFragment())
                fragmentTransaction.add(R.id.status, StatusBarFragment())
            }

            settings -> {
                fragmentManager.findFragmentById(R.id.settings_menu)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.settings, SettingsFragment())
            }

            help -> {
                fragmentManager.findFragmentById(R.id.settings_menu)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.tutorial, TutorialFragment())
            }

            tasks -> {
                fragmentManager.findFragmentById(R.id.settings_menu)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.tasks, TaskManagerFragment())
            }

            characteristics -> {
                fragmentManager.findFragmentById(R.id.settings_menu)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.characteristics, CharacteristicsFragment())
            }

            statistics -> {
                fragmentManager.findFragmentById(R.id.settings_menu)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.statistics, StatisticsFragment())
            }

            save -> thread {
                val dao = MainActivity.db.userDao()
                dao.deleteSaves()
                dao.insertSave(
                    MainActivity.Saves(
                        0,
                        Json.encodeToString(
                            MainActivity.Companion.Assets.serializer(),
                            MainActivity.assets
                        ),
                        Json.encodeToString(Player.serializer(), MainActivity.player), MainActivity.showTutorial
                    )
                )
                MainActivity.saved = true
            }

        }
        fragmentTransaction.commit()
    }
}