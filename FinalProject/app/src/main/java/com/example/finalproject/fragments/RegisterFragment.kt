package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.example.finalproject.service.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var register: Button
    private lateinit var back: Button

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back = requireView().findViewById(R.id.register_back_button)
        register = requireView().findViewById(R.id.register_button)
        back.setOnClickListener(this)
        register.setOnClickListener(this)
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        if (p0 == register) {
            val t: ProgressBar = requireView().findViewById(R.id.register_loading)
            val loginView: EditText = requireView().findViewById(R.id.login_reg)
            val emailView: EditText = requireView().findViewById(R.id.email_reg)
            val passwordView: EditText = requireView().findViewById(R.id.password_reg)
            val confirmPasswordView: EditText =
                requireView().findViewById(R.id.confirm_password_reg)
            val login: String = loginView.text.toString()
            val email: String = emailView.text.toString()
            val password: String = passwordView.text.toString()
            val confirmPassword: String = confirmPasswordView.text.toString()
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
                register.visibility = View.GONE
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password.hashCode().toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val ref = FirebaseDatabase.getInstance().getReference("Users")
                            MainActivity.player.user = User(login, email)
                            MainActivity.player.user.uID = FirebaseAuth.getInstance().uid.toString()
                            ref.child(FirebaseAuth.getInstance().uid!!)
                                .setValue(MainActivity.player.user)
                            MainActivity.player.user.logIn()
                            val fm = parentFragmentManager
                            val fr = fm.beginTransaction()
                            fr.add(R.id.chat, ChatFragment())
                            fm.findFragmentById(R.id.register)?.let { fr.remove(it) }
                            fr.commit()
                        } else
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        t.clearAnimation()
                        t.visibility = View.GONE
                        register.visibility = View.VISIBLE
                    }
            }
        } else if (p0 == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.register)!!)
            fragmentTransaction.add(R.id.log_in, SignInFragment())
            fragmentTransaction.commit()
        }
    }
}