package com.example.nailexpress.models.ui.main

import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.extension.Format
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.getDateCurrent
import com.example.nailexpress.extension.toDateUTC
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class BookingStaffForm(
    var curriculum_vitae_id: Int = 0,
    var contact_name: String = "",
    var contact_phone: String = "",
    var description: String = "",
    var address: String = "",
    var longitude: String = "",
    var salon_id: Int? = null,
    var state: String = "",
    var city: String = "",
    var zipcode: Int? = null,
    var latitude: String = "",
    var booking_by_skill: String? = null,
    var booking_by_time: String? = null,
    var work_time: String? = null,
    var booking_time_values: String? = null,
    var booking_time: String? = null,
    // custom
    @Transient var adapterSelectListItem: List<Skill> = listOf(),
    @Transient var isBookNow: Boolean = true,
    @Transient var isVisibleRecycler: Boolean = false,
    @Transient var isSelectBookingService: Boolean = true,
    @Transient var isSkillEmpty: Boolean = false,
    @Transient var isSkillByTimeEmpty: Boolean = false,
    @Transient
    var bookTime: BookingTime = BookingTime(),
    @Transient
    var date: String = getDateCurrent(Format.FORMAT_DATE),
    @Transient
    var time: String = AppConfig.TIME_DEFAULT,
) : Form {
    override fun validate() {
        handleData()
    }

    private fun handleBookingTime() {
        booking_time = "$date $time".toDateUTC()
    }

    private fun handleAdapterItems(){
        val list = adapterSelectListItem.filter {
            it.isCheck
        }.map {
            BookServiceForm(it, isTimeType = !isSelectBookingService)
        }

        if(isSelectBookingService){
            booking_by_skill = list.toString()
        }else{
            booking_by_time = list.toString()
        }
    }

    override fun handleData() {
        handleAdapterItems()
        handleBookingTime()

        if(isSelectBookingService){
            work_time = null
        }else{

        }

        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}

data class BookingTime(
    var unit: String? = null,
    var price: String? = null,
    @Transient var unitIndex: Int = 0,
) {
    fun handleData() {
        unit = unitIndex.toString()
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}


data class BookServiceForm(
    var skill_id: String = "",
    var price: String? = null,
    @SerializedName("custom_skill")
    var skill_name: String = "",
    var unit: String? = null,
    @Transient
    var price_display: String = "",
    @Transient
    var isTypeTime: Boolean = false
) {
    // chỉ lấy skill name
    constructor(service: Skill) : this() {
        skill_name = service.name
    }

    constructor(service: Skill, isTimeType: Boolean) : this() {
        skill_name = service.name
        isTypeTime = isTimeType
    }

    fun handleAddItem(isTimeType: Boolean): BookServiceForm {
        isTypeTime = isTimeType
        price_display = "$${price}"
        return this
    }

    override fun toString(): String {
        if (isTypeTime) {
            price = null
            unit = null
        }
        return Gson().toJson(this)
    }
}