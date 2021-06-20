package com.example.finalproject.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class User {
    private String login;
    private String uID;
    private String email;
    private boolean loggedIn;

    public User(){}

    public User(String login, String email) {
        this.login = login;
        this.email = email;
        loggedIn =false;
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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logIn(){
        loggedIn=true;
    }

    public void logOut(){
        loggedIn = false;
        FirebaseDatabase.getInstance().getReference("Users").
                child(uID).child("loggedIn").setValue(false);
        FirebaseAuth.getInstance().signOut();
        login="";
        email="";
        uID="";
    }

    public void setLoggedIn(boolean loggedIn){
        this.loggedIn=loggedIn;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getuID() {
        return uID;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", uID='" + uID + '\'' +
                ", email='" + email + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }
}
