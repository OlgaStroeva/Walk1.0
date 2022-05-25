package com.example.walk10.data

import java.io.Serializable

data class Ad (
    val city: String? = null,
    val tel: String? = null,
    val adress: String? = null,
    val category: String? = null,
    val animal: String? = null,
    val description: String? = null,
    val date: String? = null,
    val key: String? = null,
    val uid: String? = null,
    val isFav: Boolean = false,

    var viewCounter: String = "0",
    var callCounter: String? = "0"
)  :Serializable