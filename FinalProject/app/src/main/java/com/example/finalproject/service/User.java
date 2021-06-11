package com.example.finalproject.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalproject.fragments.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class User {
    public String login;
    public String email;
    private String password;
    public boolean logged_in;

    public User(){}

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        logged_in=false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public void log_in(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password.hashCode()+"").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logged_in = true;
                    FirebaseDatabase.getInstance().getReference("Users").
                            child(FirebaseAuth.getInstance().getUid()).child("logged_in").setValue(true);
                    Log.d("JJJJJ", MainActivity.player.getUser().isLogged_in()+"");
                }
            }
        });
    }

    public void log_out(){
        logged_in = false;
        FirebaseDatabase.getInstance().getReference("Users").
                child(FirebaseAuth.getInstance().getUid()).child("logged_in").setValue(false);
        FirebaseAuth.getInstance().signOut();
    }
}
