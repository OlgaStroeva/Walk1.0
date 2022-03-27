package com.example.walk10.act

import android.R
import android.os.Bundle
import android.os.PersistableBundle
import android.sax.RootElement
import android.view.LayoutInflater
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.example.walk10.databinding.ActivityEditAdsBinding
import com.example.walk10.dialogs.DialogSpinnerHelper
import com.example.walk10.utils.CityHelper


class EditAdsAct :AppCompatActivity() {


    private val dialog =DialogSpinnerHelper()

    lateinit var rootElement: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()


    }
    private fun init(){



    }
    //OnClicks
    fun onClickSelectCity(view : View){
        val listCity = CityHelper.getAllCities(this)
        dialog.showSpinnerDialog(this, listCity)
    }
}
