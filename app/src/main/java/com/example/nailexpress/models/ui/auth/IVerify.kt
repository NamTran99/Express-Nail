package com.example.nailexpress.models.ui.auth

import android.os.Parcelable
import com.example.nailexpress.R
import com.example.nailexpress.exception.showError
import com.example.nailexpress.extension.convertPhoneToNormalFormat
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

    fun validateStepOne(){
        Validator.checkPhone(phone, R.id.edtPhone)
    }

    fun validateRegister(){

        if(fullname.trim().isEmpty()){
            showError(errorID = R.string.error_blank_name)
        }

        if(password.trim().isEmpty()){
            showError(errorID = R.string.error_blank_password)
        }

        Validator.checkLength(password,6, R.string.error_type_pass_not_enough)
    }
}