package com.example.walk10.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

object CityHelper {
    fun getAllCities2(country: String,context: Context): ArrayList<String> {
        val tempArray = ArrayList<String>()
        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")
            val size: Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val city2Names = jsonObject.getJSONArray(country)

                for (n in 0 until city2Names.length()) {
                    tempArray.add(city2Names.getString(n))
                }

        } catch (e: IOException) {

        }
        return tempArray
    }
    fun filterListData(list: ArrayList<String>, searchText : String?): ArrayList<String> {
        val tempList = ArrayList<String>()
        tempList.clear()
        if (searchText == null){
            tempList.add("No result")
            return tempList
        }
        for (selection : String in list){
            if(selection.lowercase(Locale.ROOT).startsWith(searchText.lowercase(Locale.ROOT)))
                tempList.add(selection)
        }
        if (tempList.size == 0)
            tempList.add("No result")
        return tempList
    }
}