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
import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Loot
import com.example.finalproject.service.classes.Resistances
import com.example.finalproject.service.classes.entities.Enemy
import com.example.finalproject.service.classes.entities.Player
import com.example.finalproject.service.classes.spell.Spell
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Random
import kotlin.math.min
import kotlin.math.roundToInt

class FightFragment(private var duel: Boolean = false, private val enemyId: Int) : Fragment(),
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
    private lateinit var playerReference: DatabaseReference
    private lateinit var enemyReference: DatabaseReference
    private lateinit var duelReference: DatabaseReference
    private lateinit var dbReference: DatabaseReference
    private lateinit var chosenSpell: Spell
    private lateinit var enemy: Enemy
    private var duelNum: Int = 0
    private var playerNum: Int = 0
    private var enemyNum: Int = 0
    private var gotEnemy = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fight, container, false)
    }


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
        dbReference = FirebaseDatabase.getInstance().getReference("Duel")
        if (duel) {
            duelProgressBar.visibility = View.VISIBLE
            duelProgressBar.animate()
            dbReference.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    duelNum =
                        if (it.result.child((it.result.childrenCount - 1).toString()).childrenCount == 2L
                            || it.result.child((it.result.childrenCount - 1).toString()).childrenCount == 0L
                        ) {
                            duelReference = dbReference.child(it.result.childrenCount.toString())
                            playerNum = 0
                            enemyNum = 1
                            duelReference.addValueEventListener(this)
                            it.result.childrenCount.toInt()
                        } else {
                            duelReference =
                                dbReference.child((it.result.childrenCount - 1).toString())
                            playerNum = 1
                            enemyReference = duelReference.child(enemyNum.toString())
                            enemyReference.addValueEventListener(this)
                            enemyReference.get().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    if (it.result.value != null) {
                                        val enemyMap = it.result.value as HashMap<*, *>
                                        enemy = Enemy(
                                            enemyMap["name"] as String,
                                            enemyMap["id"].toString().toInt(),
                                            enemyMap["health"].toString().toDouble(),
                                            enemyMap["maxHealth"].toString().toDouble(),
                                            enemyMap["healthRegen"].toString().toDouble(),
                                            enemyMap["mana"].toString().toDouble(),
                                            enemyMap["maxMana"].toString().toDouble(),
                                            enemyMap["manaRegen"].toString().toDouble(),
                                            Resistances(run {
                                                val resistances = ArrayList<Double>()
                                                for (i in (enemyMap["resistances"] as HashMap<*, *>)["resistances"] as ArrayList<*>) {
                                                    resistances.add(i.toString().toDouble())
                                                }
                                                resistances
                                            }),
                                            Loot(
                                                exp = (enemyMap["loot"] as HashMap<*, *>)["exp"].toString()
                                                    .toInt(),
                                                gold = (enemyMap["loot"] as HashMap<*, *>)["gold"].toString()
                                                    .toInt()
                                            ),
                                            Damage(run {
                                                val damage = ArrayList<Double>()
                                                for (i in (enemyMap["damage"] as HashMap<*, *>)["dmg"] as ArrayList<*>) {
                                                    damage.add(i.toString().toDouble())
                                                }
                                                damage
                                            }),
                                            enemyMap["dead"].toString().toBoolean()
                                        )
                                        duelProgressBar.visibility = View.GONE
                                        gotEnemy = true
                                        updateStatus()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong, please try again later",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    exitDuel()
                                }
                            }
                            it.result.childrenCount.toInt() - 1

                        }
                    enemyImage.setImageBitmap(MainActivity.textures[5][6])
                    addPlayerToDuel()
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                    exitDuel()
                }
            }
        } else {
            enemy = Enemy(assets.enemies[enemyId]!!)
            enemyImage.setImageBitmap(MainActivity.textures[5][enemyId - 256])
            updateStatus()
        }
        playerImage.setImageBitmap(MainActivity.getAvatar())
        attack.setOnClickListener(this)
        run.setOnClickListener(this)
        castSpell.setOnClickListener(this)
        defend.setOnClickListener(this)
    }

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

    private fun addPlayerToDuel() {
        playerReference = duelReference.child(playerNum.toString())
        playerReference.setValue(Enemy(player, player.damage))
        playerReference.child("loot")
            .setValue(Loot(exp = player.experience / 10, gold = player.gold / 10))
        playerReference.child("dead").setValue(0)
    }

    override fun onClick(v: View?) {
        val fragmentManager = parentFragmentManager
        if (duel) {
            when (v) {
                attack -> {
                    if (gotEnemy) {
                        if (player.health <= 0) {
                            onClick(run)
                        }
                        player.regenerate(playerReference)
                        player.doDamage(enemy, enemyReference)
                    }
                }

                run -> {
                    exitDuel()
                    player.gold /= 10
                    player.experience /= 10

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

    private fun exitDuel() {
        val fragmentManager = parentFragmentManager
        playerReference.child("dead").setValue(1)
        player.mana = player.maxMana
        player.health = player.maxHealth
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentManager.findFragmentById(R.id.fight)
            ?.let { fragmentTransaction.remove(it) }
        fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
        fragmentTransaction.add(R.id.status, StatusBarFragment())
        fragmentTransaction.add(R.id.menu, MenuFragment())
        fragmentTransaction.commit()
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.ref == duelReference) {
            enemyReference = duelReference.child(enemyNum.toString())
            enemyReference.addValueEventListener(this)
            enemyReference.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.value != null) {
                        val enemyMap = it.result.value as HashMap<*, *>
                        enemy = Enemy(
                            enemyMap["name"] as String,
                            enemyMap["id"].toString().toInt(),
                            enemyMap["health"].toString().toDouble(),
                            enemyMap["maxHealth"].toString().toDouble(),
                            enemyMap["healthRegen"].toString().toDouble(),
                            enemyMap["mana"].toString().toDouble(),
                            enemyMap["maxMana"].toString().toDouble(),
                            enemyMap["manaRegen"].toString().toDouble(),
                            Resistances(run {
                                val resistances = ArrayList<Double>()
                                for (i in (enemyMap["resistances"] as HashMap<*, *>)["resistances"] as ArrayList<*>) {
                                    resistances.add(i.toString().toDouble())
                                }
                                resistances
                            }),
                            Loot(
                                exp = (enemyMap["loot"] as HashMap<*, *>)["exp"].toString().toInt(),
                                gold = (enemyMap["loot"] as HashMap<*, *>)["gold"].toString()
                                    .toInt()
                            ),
                            Damage(run {
                                val damage = ArrayList<Double>()
                                for (i in (enemyMap["damage"] as HashMap<*, *>)["dmg"] as ArrayList<*>) {
                                    damage.add(i.toString().toDouble())
                                }
                                damage
                            }),
                            enemyMap["dead"].toString().toBoolean()
                        )
                        duelProgressBar.visibility = View.GONE
                        gotEnemy = true
                        updateStatus()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                    exitDuel()
                }
            }
        } else if (snapshot.ref == enemyReference) {
            enemyReference.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val enemyMap = it.result.value as HashMap<*, *>
                    enemy = Enemy(
                        enemyMap["name"] as String,
                        enemyMap["id"].toString().toInt(),
                        enemyMap["health"].toString().toDouble(),
                        enemyMap["maxHealth"].toString().toDouble(),
                        enemyMap["healthRegen"].toString().toDouble(),
                        enemyMap["mana"].toString().toDouble(),
                        enemyMap["maxMana"].toString().toDouble(),
                        enemyMap["manaRegen"].toString().toDouble(),
                        Resistances(run {
                            val resistances = ArrayList<Double>()
                            for (i in (enemyMap["resistances"] as HashMap<*, *>)["resistances"] as ArrayList<*>) {
                                resistances.add(i.toString().toDouble())
                            }
                            resistances
                        }),
                        Loot(
                            exp = (enemyMap["loot"] as HashMap<*, *>)["exp"].toString().toInt(),
                            gold = (enemyMap["loot"] as HashMap<*, *>)["gold"].toString().toInt()
                        ),
                        Damage(run {
                            val damage = ArrayList<Double>()
                            for (i in (enemyMap["damage"] as HashMap<*, *>)["dmg"] as ArrayList<*>) {
                                damage.add(i.toString().toDouble())
                            }
                            damage
                        }),
                        enemyMap["dead"].toString().toBoolean()
                    )
                    if (enemy.dead) {
                        player.takeDrop(enemy)
                        exitDuel()
                    }
                    updateStatus()
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                    exitDuel()
                }
            }
        }
    }

    override fun onCancelled(error: DatabaseError) {
        exitDuel()
        Log.e("Duel error", error.message)
    }

    private inner class SpellsAdapter(val data: ArrayList<Spell> = ArrayList()) :
        RecyclerView.Adapter<SpellsAdapter.SpellViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
            return SpellViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.spells_item, parent, false)
            )
        }

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

        override fun getItemCount(): Int = data.size

        inner class SpellViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.spell_in_list)
        }
    }
}