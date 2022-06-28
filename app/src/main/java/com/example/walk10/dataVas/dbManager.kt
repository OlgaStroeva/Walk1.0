package com.example.walk10.dataVas

import com.example.walk10.data.Ad
import com.example.walk10.data.InfoItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class dbManager {
    val adArray = ArrayList<Ad>()
    val db = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth
    fun publishAd(ad : Ad, finishListener: FinishWorkListener){
        if (auth.uid != null)
            db.child(ad.key ?: "empty")
                .child(auth.uid!!).child(AD_NODE)
                .setValue(ad).addOnCompleteListener{
                    finishListener.onFinish()
                }
    }

    fun adViewed(ad: Ad){
        var counter = ad.viewCounter.toInt()
        ++counter
        if (auth.uid != null)
            db.child(ad.key ?: "empty")
                .child(INFO_NODE).setValue(InfoItem(counter.toString(), ad.callCounter))
    }

    fun onFavClick(ad:Ad, listener: FinishWorkListener){
        if(ad.isFav){
            removeFromFavs(ad, listener)
        } else {
            addToFavs(ad, listener)
        }
    }

    private fun addToFavs(ad:Ad, listener: FinishWorkListener){
        ad.key?.let {
            auth.uid?.let { it1 ->
                db.child(it).child(FAVS_NODE).child(it1).setValue(it1).addOnCompleteListener {
                    if(it.isSuccessful){
                        listener.onFinish()
                    }
                }
            }
        }
    }
    private fun removeFromFavs(ad:Ad, listener: FinishWorkListener){
        ad.key?.let {
            auth.uid?.let { it1 ->
                db.child(it).child(FAVS_NODE).child(it1).removeValue().addOnCompleteListener {
                    if(it.isSuccessful){
                        listener.onFinish()
                    }
                }
            }
        }
    }
    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?){
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children){
                    var ad: Ad? = null
                    item.children.forEach {
                        if(ad==null) ad = it.child(AD_NODE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)
                    val favCounter = item.child(FAVS_NODE).childrenCount
                    ad?.favCounter = favCounter.toString()
                    ad?.viewCounter = infoItem?.viewsCounter ?: "0"
                    ad?.callCounter = infoItem?.callCounter ?: "0"
                    if (ad!=null) adArray.add(ad!!)
                }
                readDataCallback?.readData(adArray)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getMyAds(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDb(query,readDataCallback)
    }

    fun getMyFavs(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/favs/${auth.uid}").equalTo(auth.uid)
        readDataFromDb(query,readDataCallback)
    }


    fun getAllAds(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/date")
        readDataFromDb(query,readDataCallback)
    }

    fun deleteAd(ad: Ad, listener: FinishWorkListener){
        if(ad.key==null || ad.uid==null) return
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener{
            if(it.isSuccessful) listener.onFinish()
        }
    }

    interface ReadDataCallback{
        fun readData(list: ArrayList<Ad>)
    }
    interface FinishWorkListener{
        fun onFinish()
    }

    companion object{
        const val AD_NODE = "ad"
        const val MAIN_NODE = "main"
        const val INFO_NODE = "info"
        const val FAVS_NODE = "favs"
    }

}