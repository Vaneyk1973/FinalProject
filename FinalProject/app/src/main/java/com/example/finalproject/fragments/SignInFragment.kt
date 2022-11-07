package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment(), View.OnClickListener {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var back: Button
    private lateinit var logIn: Button
    private lateinit var register: TextView
    private lateinit var restorePassword: TextView

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back= requireView().findViewById(R.id.log_in_back_button)
        logIn= requireView().findViewById(R.id.sign_in)
        email = requireView().findViewById(R.id.email)
        password = requireView().findViewById(R.id.password)
        restorePassword = requireView().findViewById(R.id.restore_password_link)
        register = requireView().findViewById(R.id.register_link)
        progressBar = requireView().findViewById(R.id.sign_in_loading)
        logIn.setOnClickListener(this)
        register.setOnClickListener(this)
        restorePassword.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        val fragmentManager = parentFragmentManager
        if (p0 == logIn) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
                email.error = "Enter a valid email address"
            else if (password.text.toString().isEmpty())
                password.error = "Enter a valid password"
            else {
                progressBar.visibility = View.VISIBLE
                progressBar.animate()
                logIn.visibility = View.GONE
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString().hashCode().toString()
                    )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            MainActivity.player.user= User("", email.text.toString())
                            MainActivity.player.user.uID = FirebaseAuth.getInstance().uid.toString()
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(MainActivity.player.user.uID).child("login")
                                .get().addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        MainActivity.player.user.login =
                                            task1.result.value.toString()
                                        val fr = fragmentManager.beginTransaction()
                                        fr.add(R.id.chat, ChatFragment())
                                        fr.remove(fragmentManager.findFragmentById(R.id.log_in)!!)
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
                            Toast.makeText(
                                context,
                                "Wrong email and/or password", Toast.LENGTH_SHORT
                            ).show()
                            logIn.visibility = View.VISIBLE
                        }
                        progressBar.clearAnimation()
                        progressBar.visibility = View.GONE
                    }
            }
        } else if (p0 == register) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.log_in)!!)
            fragmentTransaction.add(R.id.register, RegisterFragment())
            fragmentTransaction.commit()
        } else if (p0 == restorePassword) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.log_in)!!)
            fragmentTransaction.add(R.id.restore_password, RestorePasswordFragment())
            fragmentTransaction.commit()
        } else if (p0 == back) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.log_in)!!)
            fragmentTransaction.add(R.id.map, MapFragment(MainActivity.player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        }
    }
}