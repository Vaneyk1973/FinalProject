package com.example.finalproject.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.service.Message
import com.example.finalproject.service.User
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.*

class ChatMiniFragment : Fragment(), View.OnClickListener, ValueEventListener, TextView.OnEditorActionListener {

    private lateinit var ref:DatabaseReference
    private lateinit var back:Button
    private lateinit var chat:RecyclerView
    private lateinit var enterMessage: EditText
    private val messages = ArrayList<Message>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_mini, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Message")
        chat= requireView().findViewById(R.id.chat_list)
        enterMessage = requireView().findViewById(R.id.message)
        back = requireView().findViewById(R.id.chat_mini_back_button)
        ref.addValueEventListener(this)
        back.setOnClickListener(this)
        chat.layoutManager = LinearLayoutManager(context)
        enterMessage.setOnEditorActionListener(this)
        chat.scrollToPosition(messages.size - 1)
    }

    private inner class ChatAdapter(var data: ArrayList<Message>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        inner class ChatViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var user: TextView = itemView.findViewById(R.id.user_list)
            var message: TextView = itemView.findViewById(R.id.message_list)
            var time: TextView = itemView.findViewById(R.id.time_list)

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
        if (p0==back){
            val fragmentManager = parentFragmentManager
            var fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.status)!!)
            fragmentTransaction.commit()
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.commit()
        }
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        messages.clear()
        for (i in snapshot.children)
            messages.add(i.getValue(Message::class.java)!!)
        chat.adapter = ChatAdapter(messages)
        chat.scrollToPosition(messages.size - 1)
    }

    override fun onCancelled(error: DatabaseError) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p0==enterMessage){
            val m = Message(
                p0.text.toString(),
                Gson().toJson(MainActivity.player.user),
                Date().time - Calendar.getInstance().timeZone.getOffset(Date().time) * 60L * 1000
            )
            ref.child(messages.size.toString()).setValue(m)
            p0.text = ""
            return true
        }
        return false
    }
}