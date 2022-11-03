package com.example.finalproject.service.classes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class User(var login: String = "", var email: String = "") {
    var loggedIn: Boolean = false
    var uID: String = ""

    fun logIn() {
        loggedIn = true
    }

    fun logOut() {
        FirebaseDatabase.getInstance().getReference("Users").child(uID).child("loggedIn")
            .setValue(false)
        FirebaseAuth.getInstance().signOut()
        login = ""
        email = ""
        uID = ""
        loggedIn = false
    }

    override fun toString(): String = Json.encodeToString(serializer(), this)
}
