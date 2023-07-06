package com.example.nailexpress.models.ui.auth

import android.os.Parcelable
import com.example.nailexpress.R
import com.example.nailexpress.exception.resourceError
import com.example.nailexpress.exception.viewErrorCustom
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import com.example.nailexpress.models.ui.LoginForm
import com.example.nailexpress.utils.Validator
import kotlinx.parcelize.Parcelize

@Parcelize
class VerifyForm(
    var phone: String = "",
    var code: String?  = "",
    var phone_code: String = "",
    var password: String = "",
    var fullname: String = "",
    var type: Int = 1 // 1 register, 2 resetpass
) : Parcelable {
    fun handle(): VerifyForm {
        phone = phone.convertPhoneToNormalFormat()
        return this
    }

    fun validateRegister(){
        if(fullname.trim().isEmpty()){
            resourceError(R.string.error_blank_name)
        }

        if(password.trim().isEmpty()){
            resourceError(R.string.error_blank_password)
        }

        Validator.checkLength(password,8, R.string.error_type_pass_not_enough)
    }

    fun validatePhone(){
        if(phone.trim().isEmpty()){
            resourceError(R.string.error_blank_phone)
        }
        if(phone.trim().convertPhoneToNormalFormat().length < 10){
            resourceError(R.string.error_type_phone_not_enough)
        }
    }
}