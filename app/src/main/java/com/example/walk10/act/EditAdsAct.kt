package com.example.walk10.act

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.walk10.MainActivity
import com.example.walk10.R
import com.example.walk10.adapters.ImageAdapter
import com.example.walk10.data.Ad
import com.example.walk10.dataVas.dbManager
import com.example.walk10.databinding.ActivityEditAdsBinding
import com.example.walk10.dialogs.DialogSpinnerHelper
import com.example.walk10.frag.FragmentCloseInterface
import com.example.walk10.frag.ImageListFrag
import com.example.walk10.utils.CityHelper
import com.example.walk10.utils.ImagePicker
import java.time.LocalDateTime

class EditAdsAct :AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFrag : ImageListFrag? = null
    private val dialog = DialogSpinnerHelper()
    lateinit var rootElement: ActivityEditAdsBinding
    private val dbManager = dbManager()
    lateinit var imageAdapter: ImageAdapter
    var launcherMultiSelectImage: ActivityResultLauncher<Intent>? = null
    var editImagePos = 0
    private var isEditState = false
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    private fun checkEditState(){
        isEditState = isEditState()
        if(isEditState){
            ad = intent.getSerializableExtra(MainActivity.ADS_DATA) as Ad
            if(ad != null) fillView(ad!!)
        }
    }

    private fun isEditState(): Boolean {
        return intent.getBooleanExtra(MainActivity.EDIT_STATE, false)
    }

    private fun fillView(ad: Ad) = with(rootElement){
        tvCity.text = ad.city
        editTextTextTel.setText(ad.tel)
        editTextTextAdress.setText(ad.adress)
        tvCat.text = ad.category
        tvAnimal.text = ad.animal
        editTextTextDescription.setText(ad.description)
    }

    private fun init(){
        imageAdapter = ImageAdapter()
        rootElement.vpimages.adapter = imageAdapter
    }

    //OnClicks

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickPublish(view: View){
        val adTemp = fillAd()
        if(isEditState){
            dbManager.publishAd(adTemp.copy(key = ad?.key), onPublishFinish())
        } else {
            dbManager.publishAd(adTemp, onPublishFinish())
        }

    }
    private fun onPublishFinish(): dbManager.FinishWorkListener{
        return object :dbManager.FinishWorkListener{
            override fun onFinish(){
                finish()

            }

        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private  fun fillAd() : Ad{
        val ad: Ad
        val current = LocalDateTime.now()
        rootElement.apply {
            ad = Ad(tvCity.text.toString(),
                editTextTextTel.text.toString(),
                editTextTextAdress.text.toString(),
                tvCat.text.toString(),
                tvAnimal.text.toString(),
                editTextTextDescription.text.toString(),
                current.toString(),
                dbManager.db.push().key, dbManager.auth.uid
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
        if(imageAdapter.mainArray.size == 0){
            ImagePicker.getMultiImages(this, 3)
        } else {
            openChooseImageFrag(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    override fun onFragClose(list: ArrayList<Bitmap>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    fun openChooseImageFrag(newList: ArrayList<Uri>?){
        chooseImageFrag = ImageListFrag(this, newList)
        if(newList != null) chooseImageFrag?.resizeSelectedImages(newList as ArrayList<Uri>?, true, this)
        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()
    }
}
