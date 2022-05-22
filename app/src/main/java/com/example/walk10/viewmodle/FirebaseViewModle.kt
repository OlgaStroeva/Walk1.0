package com.example.walk10.viewmodle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walk10.data.Ad

class FirebaseViewModle: ViewModel() {
    private val dbManager = dbManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>>()
    fun loadAllAds() {
        dbManager.getAllAds(object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }
        })
    }
    fun loadMyAds() {
        dbManager.getMyAds(object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }
        })
    }
} {
}