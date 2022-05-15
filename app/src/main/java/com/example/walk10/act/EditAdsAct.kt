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
import android.content.pm.PackageManager
import com.example.walk10.data.Ad
import com.example.walk10.dataVas.dbManager
import com.example.walk10.utils.ImagePicker
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditAdsAct :AppCompatActivity() {
    private val dialog = DialogSpinnerHelper()
    private val isImagePermissionGranted=false
    lateinit var rootElement: ActivityEditAdsBinding
    val dbManager = dbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {
            if(data!=null){
            val returnValue = data.getStringArrayListExtra(Pix. IMAGE_RESULTS)
                Log.d("MyLog","Image :${returnValue?.get(0)}")
                Log.d("MyLog","Image:${returnValue?.get(1)}")
                Log.d("MyLog","Image:${returnValue?.get(2)}")
        }}
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this)
                } else {
                    isImagePermissionGranted=false
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker”,
                                Toast . LENGTH_LONG
                    ).show()
                }
                return
            }}}



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
    fun onClickGetImages(view: View) {
        ImagePicker.getImages(this)
    }

}
