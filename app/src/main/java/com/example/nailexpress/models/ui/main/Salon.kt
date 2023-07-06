package com.example.nailexpress.models.ui.main

import com.example.nailexpress.models.ui.AppImage

data class Salon(
    var name: String = "",
    var have_car: Int = 0, // 0 not,
    var have_place: Int = 0,
    var customer_skin_color: Int = 0,//0: all, 1:white, 2:black, 3 xi (default 0)
    var description: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var city: String = "",
    var state: String = "",
    var zipcode: Int? = null,
    var experience_years: String = "-1",
    var salonID: Int = 0,
    // custom
    val isImageEmpty: Boolean = false,
    var display_have_place:String = "",
    var display_have_car: String = "",
    var experience_years_display: String= "",
    var skinColorDisplay: String = "",
    var priceDisplay: String = "",
    var localImage: MutableList<AppImage> = mutableListOf<AppImage>(),
    var phoneDisplay: String = "",
    var listImage : List<AppImage> = listOf() // for read
    ) {
    fun validate(){

    }
}

//data class SalonForm(
//    var name: String = "",
//    var have_car: Int = 0,
//    var have_place: Int = 0,
//    var phone: String = "",
//    var experience_years: String = "",
//    var localImage: MutableList<AppImage> = mutableListOf<AppImage>(),
//    var address: String = "",
//    var latitude: Double = 0.0,
//    var longitude: Double = 0.0,
//    var state: String = "",
//    var city: String = "",
//    var zipcode: String = "",
//    var customer_skin_color: Int = 0,  //0: all, 1:white, 2:black (default 0), 3 xi
//    var description: String = ""
//)