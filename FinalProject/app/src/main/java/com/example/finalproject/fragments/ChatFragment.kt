package com.example.finalproject.fragments

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
import com.example.finalproject.service.Message
import com.example.finalproject.service.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.util.*

class ChatFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar: ProgressBar = requireView().findViewById(R.id.progressBar)
        progressBar.animate()
        val ref = FirebaseDatabase.getInstance().getReference("Messages")
        val logOut: Button = requireView().findViewById(R.id.log_out)
        val chat: RecyclerView = requireView().findViewById(R.id.chat_list)
        val enterMessage: EditText = requireView().findViewById(R.id.message)
        val back: Button = requireView().findViewById(R.id.chat_back_button)
        val messages: ArrayList<Message> = ArrayList<Message>()
        val fm = parentFragmentManager
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (i in snapshot.children)
                    messages.add(i.getValue(Message::class.java)!!)
                chat.adapter = ChatAdapter(messages)
                chat.scrollToPosition(messages.size - 1)
                progressBar.visibility = View.GONE
                Log.d("Messages", messages.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        back.setOnClickListener {
            val fr = fm.beginTransaction()
            fr.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fr.add(R.id.menu, MenuFragment())
            fr.add(R.id.status, StatusBarFragment())
            fr.remove(fm.findFragmentById(R.id.chat)!!)
            fr.commit()
        }
        chat.adapter = ChatAdapter(messages)
        chat.layoutManager = LinearLayoutManager(context)
        enterMessage.setOnEditorActionListener { v, _, _ ->
            if (v.text.toString().isNotEmpty()) {
                val m = Message(
                    v.text.toString(),
                    Gson().toJson(MainActivity.player.user),
                    Date().time - Calendar.getInstance().timeZone.getOffset(Date().time) * 60L * 1000
                )
                ref.child(messages.size.toString()).setValue(m)
                v.text = ""
            }
            false
        }
        logOut.setOnClickListener {
            MainActivity.player.user.logOut()
            val fr = fm.beginTransaction()
            fr.remove(fm.findFragmentById(R.id.chat)!!)
            fr.add(R.id.log_in, SignInFragment())
            fr.commit()
        }
        chat.scrollToPosition(messages.size - 1)
    }

    private inner class ChatAdapter(val data: ArrayList<Message>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        inner class ChatViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val user: TextView = itemView.findViewById(R.id.user_list)
            val message: TextView = itemView.findViewById(R.id.message_list)
            val time: TextView = itemView.findViewById(R.id.time_list)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            return ChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            holder.message.text = data[position].message
            var time = data[position].date / 1000 + Calendar.getInstance().timeZone.getOffset(
                Date().time
            )
            val seconds: String = if (time % 60 >= 10) (time % 60).toString() else "0${time % 60}"
            time /= 60
            val minutes: String = if (time % 60 >= 10) (time % 60).toString() else "0${time % 60}"
            time /= 60
            val hours: String = (time % 24).toString()
            val date = "$hours:$minutes:$seconds"
            holder.time.text = date
            holder.user.text = Gson().fromJson(
                data[position].user,
                User::class.java
            ).login
        }

        override fun getItemCount(): Int = data.size
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}