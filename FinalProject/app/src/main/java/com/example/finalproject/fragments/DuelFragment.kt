package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R
import com.example.finalproject.service.classes.User
import com.example.finalproject.service.classes.entities.Enemy
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.serialization.json.Json

class DuelFragment : Fragment(), View.OnClickListener {

    private lateinit var back: Button
    private lateinit var createDuelButton: Button
    private lateinit var duelList: RecyclerView
    private val duelListRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Duel")

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_duel, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = requireView().findViewById(R.id.duel_list_back_button)
        createDuelButton = requireView().findViewById(R.id.create_duel_button)
        duelList = requireView().findViewById(R.id.duel_list)
        getDuelList()
        player.loot.exp = player.experience / 10
        player.loot.gold = player.gold / 10
        duelList.layoutManager = LinearLayoutManager(context)
        back.setOnClickListener(this)
        createDuelButton.setOnClickListener(this)
    }

    /**
     * gets the list of available duels from the database
     */
    private fun getDuelList() {
        duelListRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.value != null) {
                    val duelListMap =
                        it.result.value as HashMap<String, ArrayList<HashMap<String, HashMap<String, *>>>>
                    val users = ArrayList<User>()
                    for (i in duelListMap.values) {
                        if (i[2] == null) {
                            val user = i[0]["user"]!!
                            if (user["uid"] != player.user.uID) {
                                users.add(
                                    User(
                                        user["login"].toString(),
                                        user["email"].toString(),
                                        user["loggedIn"].toString().toBoolean(),
                                        user["uid"].toString(),
                                        user["rating"].toString().toInt()
                                    )
                                )
                            }
                        }
                    }
                    duelList.adapter = DuelAdapter(users)
                }
            } else {
                Toast.makeText(
                    context,
                    "Something went wrong, please try again later",
                    Toast.LENGTH_SHORT
                ).show()
                onClick(back)
            }
        }
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(v: View?) {
        if (v == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.duel)?.let { fragmentTransaction.remove(it) }
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.commit()
        } else if (v == createDuelButton) {
            val duelRef = duelListRef.child(player.user.uID)
            duelRef.child("1").removeValue()
            duelRef.child("2").removeValue()
            duelRef.child("3").removeValue()
            duelRef.child("0").child("user").setValue(player.user)
            duelRef.child("0").child("enemy")
                .setValue(Json.encodeToString(Enemy.serializer(), Enemy(player, player.damage)))
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.duel)?.let { fragmentTransaction.remove(it) }
            fragmentTransaction.add(
                R.id.fight,
                FightFragment(duel = true, roomId = player.user.uID)
            )
            fragmentTransaction.commit()
        }
    }

    private inner class DuelAdapter(val data: ArrayList<User>) :
        RecyclerView.Adapter<DuelAdapter.ViewHolder>() {

        private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val duel: TextView = itemView.findViewById(R.id.duel_item)
        }

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.duel_item, parent, false)
            return ViewHolder(view)
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.duel.text = ">${data[position].login}: ${data[position].rating}"
            holder.duel.setOnClickListener {
                val duelRef = duelListRef.child(data[position].uID)
                duelRef.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.child("2").value == null) {
                            val fragmentManager = parentFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            duelRef.child("1").child("user").setValue(player.user)
                            duelRef.child("1").child("enemy")
                                .setValue(
                                    Json.encodeToString(
                                        Enemy.serializer(),
                                        Enemy(player, player.damage)
                                    )
                                )
                            fragmentManager.findFragmentById(R.id.duel)
                                ?.let { fragmentTransaction.remove(it) }
                            fragmentTransaction.add(
                                R.id.fight,
                                FightFragment(duel = true, roomId = data[position].uID)
                            )
                            fragmentTransaction.commit()
                        } else {
                            Toast.makeText(
                                context,
                                "This room is already full, try another one",
                                Toast.LENGTH_SHORT
                            ).show()
                            getDuelList()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Something went wrong, please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                        onClick(back)
                    }
                }
            }
        }
    }
}