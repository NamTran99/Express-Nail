package com.example.nailexpress.models.ui.main

import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.formatPrice
import com.example.nailexpress.extension.toDateUTC
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class BookingStaffForm(
    var curriculum_vitae_id: Int = 0,
    var contact_name: String = "",
    var contact_phone: String = "",
    var description: String = "",
    var address: String = "",
    var state: String = "",
    var longitude: String = "",
    var salon_id: Int = 0,
    var booking_by_skill: String? = null,
    var city: String = "",
    var zipcode: Int? = null,
    var latitude: String = "",
    var booking_by_time: String? = null,
    var booking_time_values: String? = null,
    var booking_time: String? = null,
    // custom
    @Transient var isBookNow: Boolean = true,
    @Transient var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    @Transient var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    @Transient var isVisibleRecycler: Boolean = false,
    @Transient var isSelectBookingService: Boolean = true,
    @Transient var isSkillEmpty: Boolean = false,
    @Transient var isSkillByTimeEmpty: Boolean = false,
    @Transient
    var bookTime: BookingTime = BookingTime(),
    @Transient
    var time: String = "",
    @Transient
    var date: String = "",
) : Form {
    override fun validate() {
        handleData()
    }



    fun clearListSkill() {
        listBookTime.clear()
        listBookSkill.clear()
        booking_by_skill = ""
        booking_by_time = ""
    }

    fun saveItem(item: BookServiceForm) {
        if (isSelectBookingService) {
            listBookSkill.add(item)
        } else {
            listBookTime.add(item)
        }
    }

    fun removeItem(item: BookServiceForm) {
        if (isSelectBookingService) {
            listBookSkill.remove(item)
        } else {
            listBookTime.remove(item)
        }
    }

    override fun handleData() {
        booking_time = if (time.isNotEmpty() && date.isNotEmpty()) {
            "$date $time".toDateUTC()
        } else {
            null
        }

        booking_time_values = if(!isSkillByTimeEmpty){
            bookTime.handleData()
            bookTime.toString()
        }else{
            null
        }

        booking_by_skill = if (listBookSkill.isNotEmpty()) listBookSkill.toString() else null
        booking_by_time = if (listBookTime.isNotEmpty()) listBookTime.toString() else null
        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}

data class BookingTime(
    var unit: String = "",
    var price: String = "",
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
    var price: String = "",
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

    fun handleAddItem(isTimeType: Boolean): BookServiceForm {
        isTypeTime = isTimeType
        price_display = "$${price}"
        return this
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}