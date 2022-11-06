package com.example.finalproject.service.classes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class User(var login: String = "", var email: String = "") {
    var loggedIn: Boolean = false
    var uID: String = ""
    var rating: Int = 0

    constructor(login: String, email: String, loggedIn: Boolean, uID: String, rating: Int)
            : this(login, email) {
        this.loggedIn = loggedIn
        this.uID = uID
        this.rating = rating
    }

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
