package com.example.walk10.utils

import android.view.LayoutInflater

interface AdapterCallback {
    abstract val layoutInflater: LayoutInflater

    fun onItemDelete()
}