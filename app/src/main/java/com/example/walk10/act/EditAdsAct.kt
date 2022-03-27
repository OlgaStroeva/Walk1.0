package com.example.walk10.act

import android.R
import android.os.Bundle
import android.os.PersistableBundle
import android.sax.RootElement
import android.view.LayoutInflater
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.walk10.databinding.ActivityEditAdsBinding
import com.example.walk10.dialogs.DialogSpinnerHelper
import com.example.walk10.utils.CityHelper
import com.google.firebase.auth.FirebaseAuth

class EditAdsAct :AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var rootElement: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        val listCity = CityHelper.getAllCities(this)
        val dialog = DialogSpinnerHelper()
        dialog.showSpinnerDialog(this, listCity)
    }
}