package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R
import com.example.finalproject.service.classes.User
import com.example.finalproject.service.classes.entities.Enemy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DuelFragment : Fragment(), View.OnClickListener {

    private lateinit var back: Button
    private lateinit var createDuelButton: Button
    private lateinit var duelList: RecyclerView
    private val duelListRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Duel")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_duel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = requireView().findViewById(R.id.duel_list_back_button)
        createDuelButton = requireView().findViewById(R.id.create_duel_button)
        duelList = requireView().findViewById(R.id.duel_list)
        getDuelList()
        duelList.layoutManager = LinearLayoutManager(context)
        back.setOnClickListener(this)
        createDuelButton.setOnClickListener(this)
    }

    private fun getDuelList() {
        duelListRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.value != null) {
                    val duelListMap =
                        it.result.value as HashMap<String, ArrayList<HashMap<String, HashMap<String, *>>>>
                    val users = ArrayList<User>()
                    for (i in duelListMap.values) {
                        if (i.size == 1) {
                            val user = i[0]["user"]!!
                            users.add(
                                User(
                                    user["name"].toString(),
                                    user["email"].toString(),
                                    user["loggedIn"].toString().toBoolean(),
                                    user["uID"].toString(),
                                    user["rating"].toString().toInt()
                                )
                            )
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
            duelRef.child("0").child("user").setValue(player.user)
            duelRef.child("0").child("enemy").setValue(Enemy(player, player.damage))
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.duel_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.duel.text = ">${data[position].login}: ${data[position].rating}"
            holder.duel.setOnClickListener {
                val duelRef = duelListRef.child(data[position].uID)
                duelRef.child("1").child("user").setValue(player.user)
                duelRef.child("1").child("enemy").setValue(Enemy(player, player.damage))
                val fragmentManager = parentFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentManager.findFragmentById(R.id.duel)?.let { fragmentTransaction.remove(it) }
                fragmentTransaction.add(
                    R.id.fight,
                    FightFragment(duel = true, roomId = data[position].uID)
                )
                fragmentTransaction.commit()
            }
        }
    }
}