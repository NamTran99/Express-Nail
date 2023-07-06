package com.example.nailexpress.models.ui.main

import com.example.nailexpress.R
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.google.gson.Gson

class RecruitmentForm(
    var avatar: String = "",
    var title: String = "",
    var booking_time: String = "",
    var gender: Int = 0, //0: fe
    var experience_years: String = "",
    var description: String  = "",
    var address: String  = "",
    var state: String = "",
    var city: String = "",
    var zipcode: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var contact_name: String = "",
    var contact_phone: String = "",
    var salon_id: Int = 0,
    var salary_by_skill: String = "",
    var salary_by_time: String = "",

    //custom
    var isBookingBySkill: Boolean = true,
    var date: String = "",
    var listCustom :  MutableList<BookServiceForm> = mutableListOf(),
    var time: String = "",
    var listBookTime: MutableList<BookServiceForm> = mutableListOf(),
    var listBookSkill: MutableList<BookServiceForm> = mutableListOf(),
    var isVisibleRecycler: Boolean=  false
):Form{
    override fun validate() {
        handleData()
    }

    fun clearListSkill(){
        listCustom.clear()
        listBookTime.clear()
        listBookSkill.clear()
        salary_by_skill = ""
        salary_by_time = ""
    }

    override fun handleData() {
        if(isBookingBySkill){
            listBookSkill.addAll(listCustom)
            salary_by_skill = Gson().toJson(listBookSkill)

        }else{
            listBookTime.addAll(listCustom)
            salary_by_time = Gson().toJson(listBookTime)
        }
        contact_phone = contact_phone.convertPhoneToNormalFormat()
    }
}