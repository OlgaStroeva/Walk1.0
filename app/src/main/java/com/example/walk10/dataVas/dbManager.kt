package com.example.walk10.dataVas

import com.example.walk10.data.Ad
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class dbManager {
    val db = Firebase.database.getReference("main")

    fun publishAd(ad : Ad){
        db.child(ad.key ?: "empty").setValue(ad)
    }
}