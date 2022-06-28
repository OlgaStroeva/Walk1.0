package com.example.walk10.viewmodle


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walk10.data.Ad
import com.example.walk10.dataVas.dbManager

class FirebaseViewModle: ViewModel() {
    private val dbManager = dbManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>?>()
    fun loadAllAds() {
        dbManager.getAllAds(object : dbManager.ReadDataCallback {
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }
        })
    }
    fun onFavClick(ad:Ad){
        dbManager.onFavClick(ad, object : dbManager.FinishWorkListener{
            override fun onFinish() {
                val updatedList =liveAdsData.value
                val pos = updatedList?.indexOf(ad)
                if(pos != -1){
                    pos?.let{
                        val count = ad.favCounter?.toInt()
                        val favCounter: Int
                        if(count!=null){
                            favCounter = if(ad.isFav) (count - 1) else (count + 1)
                        } else favCounter = 1
                        updatedList[pos] = updatedList[pos].copy(isFav = !ad.isFav, favCounter = favCounter.toString())
                    }
                }
                liveAdsData.postValue(updatedList)
            }

        })
    }

    fun adViewed(ad:Ad){
        dbManager.adViewed(ad)
    }

    fun loadMyAds() {
        dbManager.getMyAds(object : dbManager.ReadDataCallback {
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadMyFavs() {
        dbManager.getMyFavs(object : dbManager.ReadDataCallback {
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
