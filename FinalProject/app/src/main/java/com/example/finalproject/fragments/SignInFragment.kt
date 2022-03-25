package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back:Button = requireView().findViewById(R.id.log_in_back_button)
        val logIn:Button = requireView().findViewById(R.id.sign_in)
        val email:EditText = requireView().findViewById(R.id.email)
        val password:EditText = requireView().findViewById(R.id.password)
        val restorePassword:TextView = requireView().findViewById(R.id.restore_password_link)
        val register:TextView = requireView().findViewById(R.id.register_link)
        val progressBar:ProgressBar = requireView().findViewById(R.id.sign_in_loading)
        val fm=parentFragmentManager
        val clickListener =View.OnClickListener {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
                email.error = "Enter a valid email address"
            else if (password.text.toString().isEmpty())
                password.error = "Enter a valid password"
            else {
                progressBar.visibility = View.VISIBLE
                progressBar.animate()
                logIn.visibility=View.GONE
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.text.toString(),
                        password.text.toString().hashCode().toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            MainActivity.player.setUser(User("", email.text.toString()))
                            MainActivity.player.user.uID = FirebaseAuth.getInstance().uid.toString()
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(MainActivity.player.user.uID).child("login")
                                .get().addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        MainActivity.player.user.login =
                                            task1.result.value.toString()
                                        val fr = fm.beginTransaction()
                                        fr.add(R.id.chat, ChatFragment())
                                        fr.remove(fm.findFragmentById(R.id.log_in)!!)
                                        fr.commit()
                                    }
                                }
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(MainActivity.player.user.uID).child("loggedIn")
                                .setValue(true)
                                .addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        MainActivity.player.user.logIn()
                                    }
                                }
                        } else {
                            Toast.makeText(context,
                                "Wrong email and/or password", Toast.LENGTH_SHORT).show()
                            logIn.visibility=View.VISIBLE
                        }
                        progressBar.clearAnimation()
                        progressBar.visibility = View.GONE
                    }
            }
        }
        logIn.setOnClickListener(clickListener)
        register.setOnClickListener {
            val fr = fm.beginTransaction()
            fr.remove(fm.findFragmentById(R.id.log_in)!!)
            fr.add(R.id.register, RegisterFragment())
            fr.commit()
        }
        restorePassword.setOnClickListener {
            val fr = fm.beginTransaction()
            fr.remove(fm.findFragmentById(R.id.log_in)!!)
            fr.add(R.id.restore_password, RestorePasswordFragment())
            fr.commit()
        }
        back.setOnClickListener {
            val fr = fm.beginTransaction()
            fr.remove(fm.findFragmentById(R.id.log_in)!!)
            fr.add(R.id.map, MapFragment(MainActivity.player.mapNum))
            fr.add(R.id.status, StatusBarFragment())
            fr.add(R.id.menu, MenuFragment())
            fr.commit()
        }
    }
}