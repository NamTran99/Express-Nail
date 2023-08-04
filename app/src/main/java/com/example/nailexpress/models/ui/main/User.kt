package com.example.nailexpress.models.ui.main

import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.google.gson.annotations.SerializedName

interface Form{
    fun validate()
    fun handleData()
}

data class User(
    var id: Int = 0,
    var address: String = "",
    var avatar_url: String = "",
    var city: String = "",
    var email: String = "",
    var fullname: String = "",
    val gender: Int = 0,
    val phone_code: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var state: String = "",
    val state_code: String = "",
    var zipcode: String = "",
    @Transient
    val birthday: String = "",
    @Transient
    val token: String = "",
    @SerializedName("phone")
    var phoneDisplay: String = "",
    ): Form{
    override fun validate(){
        handleData()
    }

    override fun handleData() {
        phoneDisplay = phoneDisplay.convertPhoneToNormalFormat()
    }

}