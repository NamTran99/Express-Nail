package com.example.nailexpress.models.ui.main

import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.extension.toDateUTC

class RecruitmentForm(
    var avatar: String = "",
    var title: String = "",
    var gender: Int = 0, //0: fe
    var experience_years: String = "",
    var description: String = "",
    var address: String = "",
    var state: String = "",
    var city: String = "",
    var zipcode: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var contact_name: String = "",
    var contact_phone: String = "",
    var salon_id: Int? = null,
    var salary_by_skill: String? = null,
    var salary_by_time: String? = null,
    var booking_time: String? = null,
    var salary_time_values: String? = null,
    //custom
    @Transient
    var date: String = "",
    @Transient
    var time: String = "",
    @Transient
    var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    @Transient
    var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    @Transient var isSelectBookingService: Boolean = true,
    var isVisibleRecycler: Boolean = false,
    @Transient
    var bookTime: BookingTime = BookingTime(),
    @Transient
    var isSkillEmpty: Boolean = true,
    @Transient
    var isTimeSkillEmpty: Boolean = true,
) : Form {
    override fun validate() {
        handleData()
    }

    fun saveItem(item: BookServiceForm) {
        if (isSelectBookingService) {
            item.unit = null
            listBookSkill.add(item)
            isSkillEmpty = listBookSkill.isEmpty()
        } else {
            listBookTime.add(item)
            isTimeSkillEmpty = listBookTime.isEmpty()
        }
    }

    fun removeItem(item: BookServiceForm) {
        if (isSelectBookingService) {
            listBookSkill.remove(item)
            isSkillEmpty = listBookSkill.isEmpty()
        } else {
            listBookTime.remove(item)
            isTimeSkillEmpty = listBookTime.isEmpty()
        }
    }


    override fun handleData() {
        booking_time = if(time.isNotEmpty() && date.isNotEmpty()){
            "$date $time".toDateUTC()
        }else{
            null
        }

        salary_time_values = if(!isTimeSkillEmpty){
            bookTime.handleData()
            bookTime.toString()
        }else{
            null
        }

        salary_by_skill = if (listBookSkill.isNotEmpty()) listBookSkill.toString() else null
        salary_by_time = if (listBookTime.isNotEmpty()) listBookTime.toString() else null
        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}