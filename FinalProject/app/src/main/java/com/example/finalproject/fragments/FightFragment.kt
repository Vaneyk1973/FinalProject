package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.MainActivity.Companion.assets
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R
import com.example.finalproject.service.classes.entities.Enemy
import com.example.finalproject.service.classes.entities.Player
import com.example.finalproject.service.classes.spell.Spell
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.serialization.json.Json
import java.util.Random
import kotlin.math.roundToInt

class FightFragment(
    private val enemyId: Int = 262,
    private val duel: Boolean = false,
    private val roomId: String = ""
) : Fragment(),
    View.OnClickListener, ValueEventListener {

    private lateinit var run: Button
    private lateinit var attack: Button
    private lateinit var castSpell: Button
    private lateinit var defend: Button
    private lateinit var spells: RecyclerView
    private lateinit var yourHealth: TextView
    private lateinit var yourMana: TextView
    private lateinit var enemyHealth: TextView
    private lateinit var enemyMana: TextView
    private lateinit var duelProgressBar: ProgressBar
    private lateinit var chosenSpell: Spell
    private lateinit var enemy: Enemy
    private val duelReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Duel").child(roomId)
    private lateinit var playerRef: DatabaseReference
    private lateinit var enemyRef: DatabaseReference
    private val winRef: DatabaseReference = duelReference.child("2")
    private val moveRef: DatabaseReference = duelReference.child("3")
    private var playerNum: Int = 0
    private var enemyNum: Int = 1
    private var move: Int = 0
    private var enemyInit: Boolean = false
    private var win = false

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fight, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yourHealth = requireView().findViewById(R.id.your_health)
        yourMana = requireView().findViewById(R.id.your_mana)
        enemyHealth = requireView().findViewById(R.id.enemy_health)
        enemyMana = requireView().findViewById(R.id.enemy_mana)
        duelProgressBar = requireView().findViewById(R.id.fight_progress_bar)
        run = requireView().findViewById(R.id.run)
        attack = requireView().findViewById(R.id.attack)
        castSpell = requireView().findViewById(R.id.cast_spell)
        defend = requireView().findViewById(R.id.defend)
        duelProgressBar.visibility = View.GONE
        MainActivity.music.start(requireContext(), R.raw.fight)
        spells = requireView().findViewById(R.id.avaliable_spells)
        spells.layoutManager = LinearLayoutManager(context)
        val playerImage: ImageView = requireView().findViewById(R.id.player)
        val enemyImage: ImageView = requireView().findViewById(R.id.enemy)
        if (duel) {
            duelProgressBar.animate()
            castSpell.visibility = View.GONE
            defend.visibility = View.GONE
            duelProgressBar.visibility = View.VISIBLE
            getTheEnemy()
        } else {
            enemy = Enemy(assets.enemies[enemyId]!!)
            updateStatus()
        }
        attack.setOnClickListener(this)
        run.setOnClickListener(this)
        defend.setOnClickListener(this)
        castSpell.setOnClickListener(this)
        enemyImage.setImageBitmap(MainActivity.textures[5][enemyId - 256])
        playerImage.setImageBitmap(MainActivity.getAvatar())
    }

    /**
     * sets the enemy in case of a duel
     */
    private fun getTheEnemy() {
        if (roomId == player.user.uID) {
            playerRef = duelReference.child("0")
            enemyRef = duelReference.child("1")
            move = enemyNum
            moveRef.setValue(move)
        } else {
            playerNum = 1
            enemyNum = 0
            playerRef = duelReference.child("1")
            enemyRef = duelReference.child("0")
            move = playerNum
        }
        enemyRef.addValueEventListener(this)
        playerRef.addValueEventListener(this)
        winRef.addValueEventListener(this)
        moveRef.addValueEventListener(this)
    }

    /**
     * updates the health and mana views
     */
    private fun updateStatus() {
        var text = "${player.health.roundToInt()}/${player.maxHealth.roundToInt()}"
        yourHealth.text = text
        text = "${player.mana.roundToInt()}/${player.maxMana.roundToInt()}"
        yourMana.text = text
        text = "${enemy.health.roundToInt()}/${enemy.maxHealth.roundToInt()}"
        enemyHealth.text = text
        text = "${enemy.mana.roundToInt()}/${enemy.maxMana.roundToInt()}"
        enemyMana.text = text
    }

    /**
     * @param snapshot the snapshot of a database with changes
     * sets the action on the event if a database value has changed
     */
    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.ref == enemyRef && snapshot.child("enemy").value != null) {
            enemy = Json.decodeFromString(
                Enemy.serializer(),
                snapshot.child("enemy").value.toString()
            )
            enemyInit = true
            duelProgressBar.visibility = View.GONE
            updateStatus()
        } else if (snapshot.ref == playerRef && snapshot.child("enemy").value != null) {
            val playerAsEnemy = Json.decodeFromString(
                Enemy.serializer(),
                snapshot.child("enemy").value.toString()
            )
            player.health = playerAsEnemy.health
            player.mana = playerAsEnemy.mana
            if (player.health <= 0 && !win) {
                val fragmentManager = parentFragmentManager
                player.gold -= player.gold / 10
                player.experience -= player.experience / 10
                winRef.setValue(enemyNum)
                player.health = player.maxHealth
                player.mana = player.maxMana
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentManager.findFragmentById(R.id.fight)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.duel, DuelFragment())
                fragmentTransaction.commit()
            } else if (enemyInit) {
                updateStatus()
            }
        } else if (snapshot.ref == winRef && snapshot.value != null) {
            if (snapshot.value.toString().toInt() == playerNum) {
                win = true
                player.gold += enemy.loot.gold
                player.experience += enemy.loot.exp
                player.user.rating++
                player.health = player.maxHealth
                player.mana = player.maxMana
                player.takeDrop(enemy)
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentManager.findFragmentById(R.id.fight)
                    ?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(R.id.duel, DuelFragment())
                fragmentTransaction.commit()
            }
        } else if (snapshot.ref == moveRef && snapshot.value != null) {
            move = snapshot.value.toString().toInt()
        }
    }

    /**
     * loggs the event of the error
     */
    override fun onCancelled(error: DatabaseError) {
        Log.e("Duel error", error.message)
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(v: View?) {
        val fragmentManager = parentFragmentManager
        if (duel) {
            when (v) {
                attack -> {
                    if (move == playerNum) {
                        player.doDamage(enemy, enemyRef.child("enemy"))
                        moveRef.setValue(enemyNum)
                    } else {
                        Toast.makeText(context, "It's your opponent's move", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                run -> {
                    player.health = player.maxHealth
                    player.mana = player.maxMana
                    winRef.setValue(enemyNum)
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentManager.findFragmentById(R.id.fight)
                        ?.let { fragmentTransaction.remove(it) }
                    fragmentTransaction.add(R.id.duel, DuelFragment())
                    fragmentTransaction.commit()
                }
            }
        } else {
            when (v) {
                attack -> {
                    player.doDamage(enemy)
                    if (enemy.health <= 0) {
                        player.takeDrop(enemy)
                        assets.enemiesKilled[enemyId] =
                            assets.enemiesKilled[enemyId]?.inc() ?: 1
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentManager.findFragmentById(R.id.fight)
                            ?.let { fragmentTransaction.remove(it) }
                        fragmentTransaction.add(R.id.map, MapFragment())
                        fragmentTransaction.add(R.id.status, StatusBarFragment())
                        fragmentTransaction.add(R.id.menu, MenuFragment())
                        fragmentTransaction.commit()
                        MainActivity.music.start(requireContext(), R.raw.main)
                    }
                    enemy.attack(player)
                    updateStatus()
                    if (player.health <= 0) {
                        player = Player(2, 2)
                        MainActivity.setInitialData()
                        Toast.makeText(
                            context,
                            "You died \n All of your progress will be reset \n Better luck this time",
                            Toast.LENGTH_LONG
                        ).show()
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentManager.findFragmentById(R.id.fight)
                            ?.let {
                                fragmentTransaction.remove(it)
                                fragmentTransaction.add(R.id.map, MapFragment())
                                fragmentTransaction.add(R.id.status, StatusBarFragment())
                                fragmentTransaction.add(R.id.menu, MenuFragment())
                            }
                        fragmentTransaction.commit()
                        MainActivity.music.start(requireContext(), R.raw.main)
                    }
                    player.regenerate()
                    enemy.regenerate()
                }

                run -> {
                    val a = Random().nextInt(100)
                    if (a < 50) {
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentManager.findFragmentById(R.id.fight)
                            ?.let {
                                fragmentTransaction.remove(it)
                                fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
                                fragmentTransaction.add(R.id.status, StatusBarFragment())
                                fragmentTransaction.add(R.id.menu, MenuFragment())
                            }
                        fragmentTransaction.commit()
                    } else {
                        player.regenerate()
                        enemy.regenerate()
                        enemy.attack(player)
                        updateStatus()
                    }
                }

                castSpell -> {
                    updateStatus()
                    spells.adapter = SpellsAdapter(player.spells)
                }

                defend -> {
                    player.defend()
                    enemy.attack(player)
                    player.defend()
                    updateStatus()
                    if (player.health <= 0) {
                        player = Player(2, 2)
                        MainActivity.setInitialData()
                        Toast.makeText(
                            context,
                            "You died \n All of your progress will be reset \n Better luck this time",
                            Toast.LENGTH_LONG
                        ).show()
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                        fragmentTransaction.add(R.id.map, MapFragment())
                        fragmentTransaction.add(R.id.status, StatusBarFragment())
                        fragmentTransaction.add(R.id.menu, MenuFragment())
                        fragmentTransaction.commit()
                        MainActivity.music.start(requireContext(), R.raw.main)
                    }
                    player.regenerate()
                    enemy.regenerate()
                }
            }
        }
    }

    private inner class SpellsAdapter(val data: ArrayList<Spell> = ArrayList()) :
        RecyclerView.Adapter<SpellsAdapter.SpellViewHolder>() {

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
            return SpellViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.spells_item, parent, false)
            )
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
            holder.name.text = data[position].name
            holder.name.setOnClickListener {
                chosenSpell = data[position]
                (view!!.findViewById<View>(R.id.avaliable_spells) as RecyclerView).adapter =
                    SpellsAdapter()
                if (player.mana < data[position].manaConsumption)
                    Toast.makeText(
                        context,
                        "Not enough mana", Toast.LENGTH_SHORT
                    ).show()
                player.castSpell(enemy, chosenSpell)
                if (enemy.health <= 0) {
                    val fragmentManager = parentFragmentManager
                    player.takeDrop(enemy)
                    assets.enemiesKilled[enemyId] =
                        assets.enemiesKilled[enemyId]?.inc() ?: 1
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentManager.findFragmentById(R.id.fight)
                        ?.let { fragmentTransaction.remove(it) }
                    fragmentTransaction.add(R.id.map, MapFragment())
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                    MainActivity.music.start(requireContext(), R.raw.main)
                }
                player.regenerate()
                enemy.regenerate()
                enemy.attack(player)
                updateStatus()
            }
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size

        inner class SpellViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.spell_in_list)
        }
    }
}