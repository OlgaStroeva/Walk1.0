package com.example.walk10.utils

import androidx.appcompat.app.AppCompatActivity

object ImagePicker {
    const val REQUEST_CODE_GET_IMAGES =999
    fun getImages(context: AppCompatActivity){
        var options = Options.init()
            . setRequestCode(REQUEST_CODE_GET_IMAGES)
            .setCount(3)
            .setFrontfacing(false)
            .setMode(Options.Mode.Picture)

            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images");
    Pix.start(context,options)
    }
}