package com.example.walk10.act

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
import com.google.android.gms.tasks.OnCompleteListener
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

class EditAdsAct :AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFrag : ImageListFrag? = null
    private val dialog = DialogSpinnerHelper()
    lateinit var rootElement: ActivityEditAdsBinding
    private val dbManager = dbManager()
    lateinit var imageAdapter: ImageAdapter
    var editImagePos = 0
    private var isEditState = false
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
        checkEditState()
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

    fun onClickPublish(view: View){
        val adTemp = fillAd()
        if(isEditState){
            dbManager.publishAd(adTemp.copy(key = ad?.key), onPublishFinish())
        } else {
            uploadImages(adTemp)
        }

    }
    private fun onPublishFinish(): dbManager.FinishWorkListener{
        return object :dbManager.FinishWorkListener{
            override fun onFinish(){
                finish()

            }

        }

    }
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
                dbManager.db.push().key, dbManager.auth.uid,
                "empty"
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
            ImagePicker.getMultiImages(this, 1)
        } else {
            val imageButton: ImageButton = view.findViewById(R.id.imageButton2)
            if(imageAdapter.mainArray.size > 0){
                imageButton.isVisible = false
            }
            /*openChooseImageFrag(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)*/
        }
    }

    private fun uploadImages(adTemp : Ad){
        val byteArray = prepareImageByteArray(imageAdapter.mainArray[0])
        uploadImage(byteArray){
            dbManager.publishAd(adTemp.copy(mainImage = it.result.toString()), onPublishFinish())
        }
    }

    private fun uploadImage(byteArray:ByteArray, listener: OnCompleteListener<Uri>){
        val imStorageRef = dbManager.dbStorage
            .child(dbManager.auth.uid!!)
            .child("image_${System.currentTimeMillis()}")
            val upTask = imStorageRef.putBytes(byteArray)
        upTask.continueWithTask{
            task->imStorageRef.downloadUrl
        }.addOnCompleteListener(listener)

    }

    private fun prepareImageByteArray(bitMap: Bitmap):ByteArray{
        val outStream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG, 20, outStream)
        return outStream.toByteArray()
    }


    override fun onFragClose(list: ArrayList<Bitmap>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    fun openChooseImageFrag(newList: ArrayList<Uri>?){
        chooseImageFrag = ImageListFrag(this)
        if(newList != null) chooseImageFrag?.resizeSelectedImages(newList as ArrayList<Uri>?, true, this)
        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        //Возможно нужно вызывать update для изображений
        fm.commit()
    }
}
