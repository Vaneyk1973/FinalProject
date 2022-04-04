package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.entities.Player
import com.example.finalproject.fragments.MainActivity.Companion.player
import com.example.finalproject.service.spell.Spell
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.util.*
import kotlin.math.roundToInt

class FightFragment(private var duel: Boolean = false): Fragment(), View.OnClickListener,
    ValueEventListener, OnCompleteListener<DataSnapshot> {

    private lateinit var run: Button
    private lateinit var attack: Button
    private lateinit var castSpell: Button
    private lateinit var spells: RecyclerView
    private lateinit var playerReference:DatabaseReference
    private lateinit var enemyReference:DatabaseReference
    private var taskId:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fight, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val duelProgressBar = requireView().findViewById<ProgressBar>(R.id.fight_progress_bar)
        run = requireView().findViewById(R.id.run)
        attack = requireView().findViewById(R.id.attack)
        castSpell = requireView().findViewById(R.id.cast_spell)
        duelProgressBar.visibility = View.GONE
        MainActivity.music?.start(requireContext(), R.raw.fight)
        spells = requireView().findViewById(R.id.avaliable_spells)
        spells.layoutManager = LinearLayoutManager(context)
        val playerImage: ImageView = requireView().findViewById(R.id.player)
        val enemyImage: ImageView = requireView().findViewById(R.id.enemy)
        if (duel) {
            duelProgressBar.visibility = View.VISIBLE
            duelProgressBar.animate()
            taskId="Duel".toInt()
            FirebaseDatabase.getInstance().getReference("Duel")
                .get().addOnCompleteListener(this)
            playerImage.setImageBitmap(MainActivity.textures[5][5])
            enemyImage.setImageBitmap(MainActivity.textures[5][6])
        } else {
            playerImage.setImageBitmap(MainActivity.getAvatar())
            enemyImage.setImageBitmap(player.enemy!!.getTexture())
            updateStatus()
            attack.setOnClickListener(this)
            run.setOnClickListener(this)
            castSpell.setOnClickListener(this)
        }
    }

    private fun updateStatus() {
        val yourHealth: TextView = requireView().findViewById(R.id.your_health)
        val yourMana: TextView = requireView().findViewById(R.id.your_mana)
        val enemyHealth: TextView = requireView().findViewById(R.id.enemy_health)
        val enemyMana: TextView = requireView().findViewById(R.id.enemy_mana)
        var text = "${player.health.roundToInt()}/${player.maxHealth.roundToInt()}"
        yourHealth.text = text
        text = "${player.mana.roundToInt()}/${player.maxMana.roundToInt()}"
        yourMana.text = text
        text = "${player.enemy!!.health.roundToInt()}/${player.enemy!!.maxHealth.roundToInt()}"
        enemyHealth.text = text
        text = "${player.enemy!!.mana.roundToInt()}/${player.enemy!!.maxMana.roundToInt()}"
        enemyMana.text = text
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
                player.chooseSpell(data[position])
                (view!!.findViewById<View>(R.id.avaliable_spells) as RecyclerView).adapter =
                    SpellsAdapter()
                if (player.mana < data[position].manaConsumption)
                    Toast.makeText(
                        context,
                        "Not enough mana", Toast.LENGTH_SHORT
                    ).show()
                player.castSpell()
                player.regenerate()
                player.enemy!!.regenerate()
                player.enemy!!.fight()
                updateStatus()
                if (player.enemy!!.health <= 0) {
                    player.takeDrop()
                    val fragmentManager = parentFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment(player.mapNum))
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }
            }
        }

        override fun getItemCount(): Int = data.size

        inner class SpellViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.spell_in_list)
        }
    }

    override fun onClick(v: View?) {
        val fragmentManager = parentFragmentManager
        if (!duel){
            if (v==attack) {
                player.regenerate()
                player.enemy!!.regenerate()
                player.attack()
                player.enemy!!.fight()
                updateStatus()
                if (player.enemy!!.health <= 0) {
                    player.takeDrop()
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment())
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                    MainActivity.music?.start(requireContext(), R.raw.main)
                }
                if (player.health <= 0) {
                    player = Player(2, 2)
                    MainActivity.setInitialData(
                        resources.getXml(R.xml.items),
                        resources.getXml(R.xml.names),
                        resources.getXml(R.xml.recipes),
                        resources.getXml(R.xml.enemies),
                        resources.getXml(R.xml.locations)
                    )
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
                    MainActivity.music?.start(requireContext(), R.raw.main)
                }
            } else if (v == run) {
                if (!duel) {
                    val a = Random().nextInt(100)
                    if (a >= 50) {
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                        fragmentTransaction.add(R.id.map, MapFragment(player.mapNum))
                        fragmentTransaction.add(R.id.status, StatusBarFragment())
                        fragmentTransaction.add(R.id.menu, MenuFragment())
                        fragmentTransaction.commit()
                    } else {
                        player.regenerate()
                        player.enemy!!.regenerate()
                        player.enemy!!.fight()
                        updateStatus()
                    }
                }else{
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment(player.mapNum))
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }
            } else if (v == castSpell) {
                updateStatus()
                spells.adapter = SpellsAdapter(player.spells)
            }
        } else{
            if (v==attack) {
                player.regenerate()
                player.attack()
                
                if (player.enemy!!.health <= 0) {

                }
                if (player.health <= 0) {

                }
            } else if (v == run) {
                if (!duel) {
                    val a = Random().nextInt(100)
                    if (a >= 50) {
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                        fragmentTransaction.add(R.id.map, MapFragment(player.mapNum))
                        fragmentTransaction.add(R.id.status, StatusBarFragment())
                        fragmentTransaction.add(R.id.menu, MenuFragment())
                        fragmentTransaction.commit()
                    } else {
                        player.regenerate()
                        player.enemy!!.regenerate()
                        player.enemy!!.fight()
                        updateStatus()
                    }
                }else{
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fight)!!)
                    fragmentTransaction.add(R.id.map, MapFragment(player.mapNum))
                    fragmentTransaction.add(R.id.status, StatusBarFragment())
                    fragmentTransaction.add(R.id.menu, MenuFragment())
                    fragmentTransaction.commit()
                }
            } else if (v == castSpell) {
                updateStatus()
                spells.adapter = SpellsAdapter(player.spells)
            }
        }
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot==playerReference){
            player.health = snapshot.child("health").value as Double
            player.mana = snapshot.child("mana").value as Double
            updateStatus()
            if (player.health <= 0) {
                snapshot.ref.child("dead").setValue(true)
                player.gold = 0.0
                player.experience = 0.0
                run.callOnClick()
            }
        }
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

    override fun onComplete(task: Task<DataSnapshot>) {
        if (taskId=="Duel".toInt()){
            task.result.child("Players").child(player.user.uID)
        }
    }
}