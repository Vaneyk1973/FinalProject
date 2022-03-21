package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.entities.Enemy
import com.example.finalproject.entities.Player
import com.example.finalproject.fragments.FightFragment.SpellsAdapter.SpellViewHolder
import com.example.finalproject.service.spell.Spell
import com.google.firebase.database.*
import java.util.*

class FightFragment : Fragment {
    private var duel: Boolean

    constructor(duel: Boolean) {
        this.duel = duel
    }

    constructor() {
        duel = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fm = parentFragmentManager
        val p = getView()!!.findViewById<ProgressBar>(R.id.fight_progress_bar)
        p.visibility = View.GONE
        MainActivity.m.start(context!!, R.raw.fight)
        val run = getView()!!.findViewById<View>(R.id.run) as Button
        val attack = getView()!!.findViewById<View>(R.id.attack) as Button
        val castSpell = getView()!!.findViewById<View>(R.id.cast_spell) as Button
        val spells = getView()!!.findViewById<View>(R.id.avaliable_spells) as RecyclerView
        spells.layoutManager = LinearLayoutManager(context)
        val playerImage = getView()!!.findViewById<View>(R.id.player) as ImageView
        val enemyImage = getView()!!.findViewById<View>(R.id.enemy) as ImageView
        val yourHealth = getView()!!.findViewById<TextView>(R.id.your_health)
        val yourMana = getView()!!.findViewById<TextView>(R.id.your_mana)
        val enemyHealth = getView()!!.findViewById<TextView>(R.id.enemy_health)
        val enemyMana = getView()!!.findViewById<TextView>(R.id.enemy_mana)
        if (duel) {
            p.visibility = View.VISIBLE
            val databaseReferences = arrayOfNulls<DatabaseReference>(2)
            val enemies = arrayOfNulls<Enemy>(1)
            val player: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    MainActivity.player.health = snapshot.child("health").getValue(
                        Double::class.java
                    )!!
                    MainActivity.player.mana = snapshot.child("mana").getValue(Double::class.java)!!
                    yourHealth.text =
                        Math.round(MainActivity.player.health).toString() + "/" + Math.round(
                            MainActivity.player.maxHealth
                        )
                    yourMana.text =
                        Math.round(MainActivity.player.mana).toString() + "/" + Math.round(
                            MainActivity.player.maxMana
                        )
                    if (MainActivity.player.health <= 0) {
                        snapshot.ref.child("dead").setValue(true)
                        MainActivity.player.gold = 0.0
                        MainActivity.player.experience = 0.0
                        run.callOnClick()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            val enemy: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //if (snapshot.getValue() != null)
                    //enemies[0] = new Enemy(snapshot.getRef(), MainActivity.textures[5][6], enemyHealth, enemyMana, p);
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            FirebaseDatabase.getInstance().getReference("Duel")
                .get().addOnCompleteListener { task ->
                    if (task.result.child(task.result.childrenCount - 1.toString() + "").childrenCount == 1L) {
                        p.animate()
                        databaseReferences[1] =
                            task.result.child(task.result.childrenCount - 1.toString() + "").ref.child(
                                "0"
                            )
                        databaseReferences[0] =
                            task.result.child(task.result.childrenCount - 1.toString() + "").ref.child(
                                "1"
                            )
                        MainActivity.player.addToDuel(databaseReferences[0])
                        MainActivity.player.duelNum =
                            (task.result.childrenCount - 1.toString() + "").toInt()
                        MainActivity.player.setDuelPnum(1)
                        databaseReferences[1]!!.addValueEventListener(enemy)
                        databaseReferences[0]!!.addValueEventListener(player)
                        databaseReferences[1]!!.child("ran")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.getValue(Boolean::class.javaPrimitiveType)!!) {
                                        run.callOnClick()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        //enemies[0] = new Enemy(databaseReferences[1], MainActivity.textures[5][6], enemyHealth, enemyMana, p);
                        yourHealth.text = Math.round(MainActivity.player.health)
                            .toString() + "/" + Math.round(MainActivity.player.maxHealth)
                        yourMana.text = Math.round(MainActivity.player.mana)
                            .toString() + "/" + Math.round(MainActivity.player.maxMana)
                        run.setOnClickListener {
                            databaseReferences[0]!!.child("ran").get()
                                .addOnCompleteListener { task ->
                                    if (!task.result.getValue(Boolean::class.javaPrimitiveType)!!) {
                                        databaseReferences[0]!!.child("ran").setValue(true)
                                        val fragmentTransaction = fm.beginTransaction()
                                        fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                                        fragmentTransaction.add(
                                            R.id.map,
                                            MapFragment(
                                                MainActivity.player.mapNum
                                            )
                                        )
                                        fragmentTransaction.add(R.id.status, StatusBarFragment())
                                        fragmentTransaction.add(R.id.menu, MenuFragment())
                                        fragmentTransaction.commit()
                                    }
                                }
                        }
                        attack.setOnClickListener { MainActivity.player.attack() }
                    } else {
                        val a = (task.result.childrenCount.toString() + "").toInt()
                        databaseReferences[0] = task.result.child(a.toString() + "").ref.child("0")
                        MainActivity.player.addPlayerToDuel(databaseReferences[0])
                        MainActivity.player.duelNum = (a.toString() + "").toInt()
                        MainActivity.player.setDuelPnum(0)
                        yourHealth.text = Math.round(MainActivity.player.health)
                            .toString() + "/" + Math.round(MainActivity.player.maxHealth)
                        yourMana.text = Math.round(MainActivity.player.mana)
                            .toString() + "/" + Math.round(MainActivity.player.maxMana)
                        run.setOnClickListener {
                            databaseReferences[0]!!.child("ran").get()
                                .addOnCompleteListener { task ->
                                    if (!task.result.getValue(Boolean::class.javaPrimitiveType)!!) {
                                        databaseReferences[0]!!.child("ran").setValue(true)
                                        val fragmentTransaction =
                                            parentFragmentManager.beginTransaction()
                                        fragmentTransaction.remove(
                                            parentFragmentManager.findFragmentById(
                                                R.id.fight
                                            )!!
                                        )
                                        fragmentTransaction.add(
                                            R.id.map,
                                            MapFragment(
                                                MainActivity.player.mapNum
                                            )
                                        )
                                        fragmentTransaction.add(R.id.status, StatusBarFragment())
                                        fragmentTransaction.add(R.id.menu, MenuFragment())
                                        fragmentTransaction.commit()
                                    }
                                }
                        }
                        attack.setOnClickListener {
                            task.result.child(task.result.childrenCount.toString() + "")
                                .ref.child("1").get()
                                .addOnCompleteListener { task -> if (task.isSuccessful && task.result.value != null) MainActivity.player.attack() }
                        }
                        databaseReferences[0]!!.addValueEventListener(player)
                        task.result.child(task.result.childrenCount.toString() + "")
                            .ref.child("1").addValueEventListener(enemy)
                        task.result.child(task.result.childrenCount.toString() + "")
                            .ref.child("1").child("ran")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.value != null && snapshot.getValue(
                                            Boolean::class.javaPrimitiveType
                                        )!!
                                    ) {
                                        run.callOnClick()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        p.animate()
                    }
                }
            playerImage.setImageBitmap(MainActivity.textures[5][5])
            enemyImage.setImageBitmap(MainActivity.textures[5][6])
        } else {
            playerImage.setImageBitmap(MainActivity.textures[5][5])
            enemyImage.setImageBitmap(MainActivity.player.enemy!!.texture)
            yourHealth.text = Math.round(MainActivity.player.health).toString() + "/" + Math.round(
                MainActivity.player.maxHealth
            )
            yourMana.text = Math.round(MainActivity.player.mana).toString() + "/" + Math.round(
                MainActivity.player.maxMana
            )
            enemyHealth.text =
                MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
            enemyMana.text =
                MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
            attack.setOnClickListener {
                MainActivity.player.regenerate()
                MainActivity.player.enemy!!.regenerate()
                MainActivity.player.attack()
                MainActivity.player.enemy!!.fight()
                yourHealth.text = Math.round(MainActivity.player.health)
                    .toString() + "/" + Math.round(MainActivity.player.maxHealth)
                yourMana.text = Math.round(MainActivity.player.mana)
                    .toString() + "/" + Math.round(MainActivity.player.maxMana)
                enemyHealth.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                enemyMana.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                if (MainActivity.player.enemy!!.health <= 0) {
                    MainActivity.player.take_drop()
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(
                        R.id.map,
                        MapFragment()
                    )
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                    MainActivity.m.start(context!!, R.raw.main)
                }
                if (MainActivity.player.health <= 0) {
                    MainActivity.player = Player(2, 2)
                    MainActivity.setInitialData(
                        resources.getXml(R.xml.items),
                        resources.getXml(R.xml.names),
                        resources.getXml(R.xml.recipes),
                        resources.getXml(R.xml.enemies),
                        resources.getXml(R.xml.locations)
                    )
                    Toast.makeText(
                        context,
                        "You died \n All of your progress will be deleted \n Better luck this time",
                        Toast.LENGTH_LONG
                    ).show()
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(
                        R.id.map,
                        MapFragment()
                    )
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                    MainActivity.m.start(context!!, R.raw.main)
                }
            }
            run.setOnClickListener {
                val a = Random().nextInt(100)
                MainActivity.player.regenerate()
                MainActivity.player.enemy!!.regenerate()
                MainActivity.player.enemy!!.fight()
                Log.d("KK", MainActivity.player.enemy!!.name)
                yourHealth.text = Math.round(MainActivity.player.health)
                    .toString() + "/" + Math.round(MainActivity.player.maxHealth)
                yourMana.text = Math.round(MainActivity.player.mana)
                    .toString() + "/" + Math.round(MainActivity.player.maxMana)
                enemyHealth.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                enemyMana.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                if (a >= 50) {
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(
                        R.id.map,
                        MapFragment(MainActivity.player.mapNum)
                    )
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }
            }
            castSpell.setOnClickListener {
                Log.d("KK", MainActivity.player.enemy!!.name)
                yourHealth.text = Math.round(MainActivity.player.health)
                    .toString() + "/" + Math.round(MainActivity.player.maxHealth)
                yourMana.text = Math.round(MainActivity.player.mana)
                    .toString() + "/" + Math.round(MainActivity.player.maxMana)
                enemyHealth.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                enemyMana.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                spells.adapter =
                    SpellsAdapter(MainActivity.player.spells)
            }
        }
    }

    internal inner class SpellsAdapter(data: ArrayList<Spell>?) :
        RecyclerView.Adapter<SpellViewHolder>() {
        var data = ArrayList<Spell>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
            return SpellViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.spells_item, parent, false)
            )
        }

        override fun onBindViewHolder(
            holder: SpellViewHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            holder.name.text = data[position].name
            holder.name.setOnClickListener {
                MainActivity.player.choose_spell(data[position])
                (view!!.findViewById<View>(R.id.avaliable_spells) as RecyclerView).adapter =
                    SpellsAdapter(ArrayList<Spell>())
                if (MainActivity.player.mana < data[position].manaConsumption) Toast.makeText(
                    context, "Not enough mana", Toast.LENGTH_SHORT
                ).show()
                MainActivity.player.castSpell()
                MainActivity.player.regenerate()
                MainActivity.player.enemy!!.regenerate()
                MainActivity.player.enemy!!.fight()
                val your_health = view!!.findViewById<TextView>(R.id.your_health)
                val your_mana = view!!.findViewById<TextView>(R.id.your_mana)
                val enemy_health = view!!.findViewById<TextView>(R.id.enemy_health)
                val enemy_mana = view!!.findViewById<TextView>(R.id.enemy_mana)
                your_health.text = Math.round(MainActivity.player.health)
                    .toString() + "/" + Math.round(MainActivity.player.maxHealth)
                your_mana.text = Math.round(MainActivity.player.mana)
                    .toString() + "/" + Math.round(MainActivity.player.maxMana)
                enemy_health.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                enemy_mana.text =
                    MainActivity.player.enemy!!.health.toString() + "/" + MainActivity.player.enemy!!.maxHealth
                if (MainActivity.player.enemy!!.health <= 0) {
                    MainActivity.player.take_drop()
                    val fm = parentFragmentManager
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(
                        R.id.map,
                        MapFragment(MainActivity.player.mapNum)
                    )
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        internal inner class SpellViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView

            init {
                name = itemView.findViewById(R.id.spell_in_list)
            }
        }

        init {
            this.data.addAll(data!!)
        }
    }
}