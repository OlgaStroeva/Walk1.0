package com.example.walk10.viewmodle


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walk10.data.Ad
import com.example.walk10.dataVas.dbManager

class FirebaseViewModle: ViewModel() {
    private val dbManager = dbManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>>()
    fun loadAllAds() {
        dbManager.getAllAds(object : dbManager.ReadDataCallback {
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }
        })
    }
    fun loadMyAds() {
        dbManager.getMyAds(object : dbManager.ReadDataCallback {
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }
        })
    }
    fun deleteItem(ad:Ad){
        dbManager.deleteAd(ad, object :dbManager.FinishWorkListener{
            override fun onFinish() {
                val updatedList =liveAdsData.value
                updatedList?.remove(ad)
                liveAdsData.postValue(updatedList)

            }

        })

    }
}
