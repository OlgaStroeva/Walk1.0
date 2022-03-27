package com.example.walk10.dataVas

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class dbManager {
    val db = Firebase.database.getReference("main")

    fun publishAd(){
        db.setValue("Hello")
    }
}