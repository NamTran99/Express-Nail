package com.example.nailexpress.models.ui.main

import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.google.gson.annotations.SerializedName

data class PasswordForm(
    var current_password: String = "",
    var new_password: String = "",

    @Transient
    var retype_pass: String = "",

    ): Form{
    override fun validate(){
        handleData()
    }

    override fun handleData() {
    }

}