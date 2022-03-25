package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back:Button = requireView().findViewById(R.id.register_back_button)
        val register:Button = requireView().findViewById(R.id.register_button)
        val loginView:EditText = requireView().findViewById(R.id.login_reg)
        val emailView:EditText = requireView().findViewById(R.id.email_reg)
        val passwordView:EditText = requireView().findViewById(R.id.password_reg)
        val confirmPasswordView:EditText = requireView().findViewById(R.id.confirm_password_reg)
        var login:String
        var email:String
        var password:String
        var confirmPassword:String
        val t:ProgressBar = requireView().findViewById(R.id.register_loading)
        back.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.remove(parentFragmentManager.findFragmentById(R.id.register)!!)
            fr.add(R.id.log_in, SignInFragment())
            fr.commit()
        }
        register.setOnClickListener {
            login = loginView.text.toString()
            email= emailView.text.toString()
            password = passwordView.text.toString()
            confirmPassword = confirmPasswordView.text.toString()
            if (login.isEmpty()) {
                loginView.error = "Login is required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailView.error = "Enter a valid email"
            } else if (password.isEmpty()) {
                passwordView.error = "Password is required"
            } else if (password.length < 8) {
                passwordView.error = "Password should at least 8 characters long"
            } else if (confirmPassword != password) {
                confirmPasswordView.error = "Passwords should match"
            } else {
                t.visibility = View.VISIBLE
                t.animate()
                register.visibility=View.GONE
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password.hashCode().toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val ref = FirebaseDatabase.getInstance().getReference("Users")
                            MainActivity.player.setUser(User(login, email))
                            MainActivity.player.user.uID = FirebaseAuth.getInstance().uid.toString()
                            ref.child(FirebaseAuth.getInstance().uid!!)
                                .setValue(MainActivity.player.user)
                            MainActivity.player.user.logIn()
                            val fm=parentFragmentManager
                            val fr = fm.beginTransaction()
                            fr.add(R.id.chat, ChatFragment())
                            fr.remove(fm.findFragmentById(R.id.register)!!)
                            fr.commit()
                        }
                        t.clearAnimation()
                        t.visibility = View.GONE
                        register.visibility=View.VISIBLE
                    }
            }
        }
    }
}