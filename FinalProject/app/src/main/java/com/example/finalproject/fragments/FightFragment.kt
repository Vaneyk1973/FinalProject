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
import com.example.finalproject.entities.Player
import com.example.finalproject.service.spell.Spell
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class FightFragment(private var duel:Boolean=false) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fight, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fm = parentFragmentManager
        val duelProgressBar= requireView().findViewById<ProgressBar>(R.id.fight_progress_bar)
        duelProgressBar.visibility = View.GONE
        MainActivity.music?.start(requireContext(), R.raw.fight)
        val run:Button = requireView().findViewById(R.id.run)
        val attack:Button = requireView().findViewById(R.id.attack)
        val castSpell:Button = requireView().findViewById(R.id.cast_spell)
        val spells:RecyclerView = requireView().findViewById(R.id.avaliable_spells)
        spells.layoutManager = LinearLayoutManager(context)
        val playerImage:ImageView = requireView().findViewById(R.id.player)
        val enemyImage:ImageView = requireView().findViewById(R.id.enemy)
        if (duel) {
            //TODO
            /*duelProgressBar.visibility = View.VISIBLE
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
                        duelProgressBar.animate()
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
                        duelProgressBar.animate()
                    }
                }
            playerImage.setImageBitmap(MainActivity.textures[5][5])
            enemyImage.setImageBitmap(MainActivity.textures[5][6])*/
        } else {
            playerImage.setImageBitmap(MainActivity.getAvatar())
            enemyImage.setImageBitmap(MainActivity.player.enemy!!.getTexture())
            updateStatus()
            attack.setOnClickListener {
                Log.d("Attack", MainActivity.player.damage.toString())
                MainActivity.player.regenerate()
                MainActivity.player.enemy!!.regenerate()
                MainActivity.player.attack()
                MainActivity.player.enemy!!.fight()
                updateStatus()
                if (MainActivity.player.enemy!!.health <= 0) {
                    MainActivity.player.takeDrop()
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment())
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                    MainActivity.music?.start(requireContext(), R.raw.main)
                }
                if (MainActivity.player.health <= 0) {
                    MainActivity.player = Player(2, 2)
                    MainActivity.setInitialData(
                        resources.getXml(R.xml.items),
                        resources.getXml(R.xml.names),
                        resources.getXml(R.xml.recipes),
                        resources.getXml(R.xml.enemies),
                        resources.getXml(R.xml.locations))
                    Toast.makeText(context,
                        "You died \n All of your progress will be deleted \n Better luck this time",
                        Toast.LENGTH_LONG).show()
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment())
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                    MainActivity.music?.start(requireContext(), R.raw.main)
                }
            }
            run.setOnClickListener {
                val a = Random().nextInt(100)
                if (a >= 50) {
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }else{
                    MainActivity.player.regenerate()
                    MainActivity.player.enemy!!.regenerate()
                    MainActivity.player.enemy!!.fight()
                    updateStatus()
                }
            }
            castSpell.setOnClickListener {
                updateStatus()
                spells.adapter = SpellsAdapter(MainActivity.player.spells)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateStatus(){
        val yourHealth:TextView = requireView().findViewById(R.id.your_health)
        val yourMana:TextView = requireView().findViewById(R.id.your_mana)
        val enemyHealth:TextView = requireView().findViewById(R.id.enemy_health)
        val enemyMana:TextView = requireView().findViewById(R.id.enemy_mana)
        yourHealth.text = "${MainActivity.player.health.roundToInt()}/${MainActivity.player.maxHealth.roundToInt()}"
        yourMana.text = "${MainActivity.player.mana.roundToInt()}/${MainActivity.player.maxMana.roundToInt()}"
        enemyHealth.text ="${MainActivity.player.enemy!!.health.roundToInt()}/${MainActivity.player.enemy!!.maxHealth.roundToInt()}"
        enemyMana.text ="${MainActivity.player.enemy!!.mana.roundToInt()}/${MainActivity.player.enemy!!.maxMana.roundToInt()}"
    }

    private inner class SpellsAdapter(val data: ArrayList<Spell> = ArrayList()) : RecyclerView.Adapter<SpellsAdapter.SpellViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
            return SpellViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.spells_item, parent, false))
        }

        override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
            holder.name.text = data[position].name
            holder.name.setOnClickListener {
                MainActivity.player.chooseSpell(data[position])
                (view!!.findViewById<View>(R.id.avaliable_spells) as RecyclerView).adapter = SpellsAdapter()
                if (MainActivity.player.mana < data[position].manaConsumption)
                    Toast.makeText(context,
                        "Not enough mana", Toast.LENGTH_SHORT).show()
                MainActivity.player.castSpell()
                MainActivity.player.regenerate()
                MainActivity.player.enemy!!.regenerate()
                MainActivity.player.enemy!!.fight()
                updateStatus()
                if (MainActivity.player.enemy!!.health <= 0) {
                    MainActivity.player.takeDrop()
                    val fm = parentFragmentManager
                    val fragmentTransaction = fm.beginTransaction()
                    fragmentTransaction.remove(fm.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNum))
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }
            }
        }

        override fun getItemCount(): Int=data.size

        inner class SpellViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.spell_in_list)
        }
    }
}