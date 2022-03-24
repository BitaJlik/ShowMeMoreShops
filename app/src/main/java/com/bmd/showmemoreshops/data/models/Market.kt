package com.bmd.showmemoreshops.data.models

import android.media.Image


data class Market(
    var nameMarket: String = "NULL",
    var categories: HashMap<String,Category> = HashMap(),
    var comments: HashMap<String,Comment> = HashMap(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var owner: String= "NULL",
    var email: String= "NULL",
    var userUUID: String= "NULL",
    var marketUUID : String = java.util.UUID.randomUUID().toString(),
    var openTime : String= "NULL",
    var image : Image? = null,
    var sizeMarket : SizeMarket = SizeMarket.SMALL
)

{
    enum class SizeMarket { SMALL, MEDIUM, LARGE, BIG }
}