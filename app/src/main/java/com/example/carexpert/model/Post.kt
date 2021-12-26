package com.example.carexpert.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    var username : String,
    var date : String,
    var time : String,
    var kota : String,
    var provinsi : String,
    var title : String,
    var post : String
): Parcelable
