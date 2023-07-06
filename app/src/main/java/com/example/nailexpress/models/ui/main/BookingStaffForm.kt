package com.example.nailexpress.models.ui.main

import android.content.Context
import com.example.nailexpress.R
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class BookingStaffForm(
    var curriculum_vitae_id: Int = 0,
    var contact_name: String = "",
    var contact_phone: String = "",
    var description: String = "",
    var address: String = "",
    var state: String = "",
    var city: String = "",
    var zipcode: Int? = null,
    var latitude: String = "",
    var longitude: String = "",
    var booking_by_skill: String = "",
    var booking_by_time: String = "",
    var salon_id: Int  = 0,
    // custom
    @Transient
    var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    @Transient
    var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    @Transient
    var isVisibleRecycler : Boolean = false,
    @Transient
    var listCustom :  MutableList<BookServiceForm> = mutableListOf(),
    @Transient
    var isBookingBySkill: Boolean = true,
) : Form {
    override fun validate() {
        handleData()
    }

    fun clearListSkill(){
        listCustom.clear()
        listBookTime.clear()
        listBookSkill.clear()
        booking_by_skill = ""
        booking_by_time = ""
    }

    override fun handleData() {
        if(isBookingBySkill){
            listBookSkill.addAll(listCustom)
            booking_by_skill = Gson().toJson(listBookSkill)

        }else{
            listBookTime.addAll(listCustom)
            booking_by_time = Gson().toJson(listBookTime)
        }
        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}

data class BookServiceForm(
    var skill_id: String = "",
    var price: String = "",
    @SerializedName("custom_skill")
    var skill_name: String = "",
    var unit: String = "",
    //Display
    @Transient
    var unit_display: String = "",
    @Transient
    var unitIndex: Int = 0,
    ) {
    constructor(service: Service) : this() {
        skill_name = service.name
    }

    fun handleToDisplayUI(context: Context):  BookServiceForm{
        unit = unitIndex.toString()
        unit_display = if(unitIndex in 1..5){
            val temp = when (unitIndex) {
                HOUR -> R.string.time_type_1
                DAY -> R.string.time_type_2
                WEEK -> R.string.time_type_3
                MONTH -> R.string.time_type_4
                YEAR -> R.string.time_type_5
                else -> R.string.time_type_1
            }
            val suffix = context.getString(temp)
            "$${price}${suffix}"
        }else{
            "$${price}"
        }
        return this
    }

    companion object {
        const val HOUR = 1
        const val DAY = 2
        const val WEEK = 3
        const val MONTH = 4
        const val YEAR = 5
    }
}