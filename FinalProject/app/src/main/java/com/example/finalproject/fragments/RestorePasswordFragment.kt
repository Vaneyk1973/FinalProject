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
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth

class RestorePasswordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restore_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back:Button = requireView().findViewById(R.id.reset_password_back_button)
        val resetPassword: Button = requireView().findViewById(R.id.reset_password_button)
        val email:EditText = requireView().findViewById(R.id.restore_password_email)
        val progressBar:ProgressBar = requireView().findViewById(R.id.progressBar2)
        progressBar.visibility = View.GONE
        resetPassword.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            progressBar.animate()
            val emailTxt:String= email.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches())
                email.error = "Enter a valid email address"
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailTxt)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(context, "We've sent you an email", Toast.LENGTH_SHORT).show()
                            back.callOnClick()
                        }
                    }
            }
        }
        back.setOnClickListener {
            val fr = parentFragmentManager.beginTransaction()
            fr.add(R.id.log_in, SignInFragment())
            fr.remove(parentFragmentManager.findFragmentById(R.id.restore_password)!!)
            fr.commit()
        }
    }
}