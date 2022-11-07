package com.example.finalproject.fragments

import android.icu.util.Calendar
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.classes.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Integer.min
import java.util.Date

class ChatFragment : Fragment(), View.OnClickListener, ValueEventListener,
    TextView.OnEditorActionListener {

    private lateinit var backButton: Button
    private lateinit var logOutButton: Button
    private lateinit var chatMessagesRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var enterMessageView: EditText

    private var messagesList: ArrayList<Message> = ArrayList()

    private val chatDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = requireView().findViewById(R.id.progressBar)
        progressBar.animate()
        chatMessagesRecyclerView = requireView().findViewById(R.id.chat_list)
        enterMessageView = requireView().findViewById(R.id.message)
        logOutButton = requireView().findViewById(R.id.log_out)
        backButton = requireView().findViewById(R.id.chat_back_button)
        backButton.setOnClickListener(this)
        logOutButton.setOnClickListener(this)
        chatDatabaseReference.addValueEventListener(this)
        enterMessageView.setOnEditorActionListener(this)
        chatDatabaseReference.get().addOnCompleteListener {
            onDataChange(it.result)
        }
        chatMessagesRecyclerView.layoutManager = LinearLayoutManager(context)
        chatMessagesRecyclerView.scrollToPosition(min(messagesList.size - 1, 0))
    }

    private inner class ChatAdapter(val data: List<Message>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        inner class ChatViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val userLogin: TextView = itemView.findViewById(R.id.user_list)
            val messageText: TextView = itemView.findViewById(R.id.message_list)
            val timeSent: TextView = itemView.findViewById(R.id.time_list)
        }

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            return ChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
            )
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            holder.messageText.text = data[position].messageText
            var time = data[position].date / 1000 + Calendar.getInstance().timeZone.getOffset(
                Date().time
            )
            val seconds: String = if (time % 60 >= 10) (time % 60).toString() else "0${time % 60}"
            time /= 60
            val minutes: String = if (time % 60 >= 10) (time % 60).toString() else "0${time % 60}"
            time /= 60
            val hours: String = (time % 24).toString()
            val date = "$hours:$minutes:$seconds"
            holder.timeSent.text = date
            holder.userLogin.text = data[position].userLogin
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        if (p0 == logOutButton) {
            MainActivity.player.user.logOut()
            val chatFragmentTransaction = parentFragmentManager.beginTransaction()
            chatFragmentTransaction.remove(parentFragmentManager.findFragmentById(R.id.chat)!!)
            chatFragmentTransaction.add(R.id.log_in, SignInFragment())
            chatFragmentTransaction.commit()
        } else if (p0 == backButton) {
            val chatFragmentTransaction = parentFragmentManager.beginTransaction()
            chatFragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
            chatFragmentTransaction.add(R.id.menu, MenuFragment())
            chatFragmentTransaction.add(R.id.status, StatusBarFragment())
            chatFragmentTransaction.remove(parentFragmentManager.findFragmentById(R.id.chat)!!)
            chatFragmentTransaction.commit()
        }
    }

    /**
     * @param snapshot the snapshot of a database with changes
     * sets the action on the event if a database value has changed
     */
    override fun onDataChange(snapshot: DataSnapshot) {
        messagesList.clear()
        for (i in snapshot.children)
            messagesList.add(i.getValue(Message::class.java)!!)
        chatMessagesRecyclerView.adapter = ChatAdapter(messagesList)
        chatMessagesRecyclerView.scrollToPosition(messagesList.size - 1)
        progressBar.visibility = View.GONE
    }

    /**
     * @param error the thrown error
     * notifies the player about the error
     */
    override fun onCancelled(error: DatabaseError) {
        Toast.makeText(context, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show()
    }

    /**
     * @param messageEnterView the view that has been edited
     * sets the action on the event of editing a view
     */
    override fun onEditorAction(messageEnterView: TextView?, p1: Int, p2: KeyEvent?): Boolean =
        if (messageEnterView!!.text.toString().isNotEmpty()) {
            val message = Message(
                messageEnterView.text.toString(),
                MainActivity.player.user.login,
                Date().time - Calendar.getInstance().timeZone.getOffset(Date().time) * 60L * 1000
            )
            chatDatabaseReference.child(messagesList.size.toString()).setValue(message)
            messageEnterView.text = ""
            true
        } else false
}