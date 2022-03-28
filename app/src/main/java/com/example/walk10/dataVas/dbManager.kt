package com.example.walk10.dataVas

import com.example.walk10.data.Ad
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class dbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth
    fun publishAd(ad : Ad){
       if (auth.uid != null)
           db.child(ad.key ?: "empty").child(auth.uid!!).setValue(ad)
    }
}