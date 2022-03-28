package com.example.walk10.act

import android.content.ClipData
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.walk10.R
import com.example.walk10.databinding.ActivityEditAdsBinding
import com.example.walk10.dialogs.DialogSpinnerHelper
import com.example.walk10.utils.CityHelper
import android.content.Intent
import com.example.walk10.data.Ad
import com.example.walk10.dataVas.dbManager

class EditAdsAct :AppCompatActivity() {
    private val dialog = DialogSpinnerHelper()
    lateinit var rootElement: ActivityEditAdsBinding
    val dbManager = dbManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    private fun init(){
    }
    //OnClicks

    fun onClickPublish(view: View){
        dbManager.publishAd(fillAd())
    }
    private  fun fillAd() : Ad{
        val ad: Ad
        rootElement.apply {
            ad = Ad(tvCity.text.toString(),
                editTextTextTel.text.toString(),
                editTextTextAdress.text.toString(),
                tvCat.text.toString(),
                tvAnimal.text.toString(),
                editTextTextDescription.text.toString(),
                dbManager.db.push().key
            )
            return ad
        }
    }

    fun onClickSelectCat(view: View) {
            val listCity2 = resources.getStringArray(R.array.category).toMutableList() as ArrayList
            dialog.showSpinnerDialog(this, listCity2, rootElement.tvCat)
    }

    fun onClickSelectAnimal(view: View) {
        val listCity2 = resources.getStringArray(R.array.animals).toMutableList() as ArrayList
        dialog.showSpinnerDialog(this, listCity2, rootElement.tvAnimal)
    }

    fun onClickSelectCity(view: View) {
        val selectedCity2 = "Россия"
        if (selectedCity2 != getString(R.string.select_city)){
        val listCity2 = CityHelper.getAllCities2(selectedCity2,this)
        dialog.showSpinnerDialog(this, listCity2, rootElement.tvCity)
    } else {
        Toast.makeText(this, "No city selected", Toast.LENGTH_LONG).show()
    }
    }
}
