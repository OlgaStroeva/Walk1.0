package com.example.walk10.dataVas

import android.app.DownloadManager
import com.example.walk10.data.Ad
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.temporal.TemporalQuery

class dbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth
    fun publishAd(ad : Ad, finishListener: FinishWorkListener){
        if (auth.uid != null)
            db.child(ad.key ?: "empty").child(auth.uid!!).child("ad")
                .setValue(ad).addOnCompleteListener{
                    finishListener.onFinish()
                }
    }

    fun getMyAds(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        realDataFromDb(query,readDataCallback)
    }
    /*fun getAllAds(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.key + "ad/price")
        realDataFromDb(query,readDataCallback)
    }
    тут делается сортировка по цене, но так как у нас её нет не знаю пока на что заменить
    и нужен ли этот вообще */

    fun deleteAd(ad: Ad, listener: FinishWorkListener){
        if(ad.key==null || ad.uid==null) return
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener{
            if(it.isSuccessful) listener.onFinish()
        }
    }

   private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?){
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children){
                    val ad = item.children.iterator().next().child("ad").getValue(Ad::class.java)
                    if (ad!=null) adArray.add(ad)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    interface ReadDataCallback{
        fun readData(list: ArrayList<Ad>)
    }
    interface FinishWorkListener{
        fun onFinish()
    }
}