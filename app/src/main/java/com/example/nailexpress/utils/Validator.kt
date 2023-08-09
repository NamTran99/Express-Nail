package com.example.nailexpress.utils;

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.example.nailexpress.R
import com.example.nailexpress.exception.showError
import com.example.nailexpress.extension.convertPhoneToNormalFormat


object Validator {
    const val GenderDefault = -1

    fun checkEmpty(content: String, @StringRes errorId: Int? = null, @IdRes viewID: Int? = null) {
        if (content.trim().isBlank()) {
            showError(viewID, errorId ?: R.string.error_empty_field)
        }
    }

    fun checkLength(
        text: String,
        length: Int,
        @StringRes errorId: Int,
        @IdRes viewID: Int? = null
    ) {
        if (text.trim().length < length) showError(viewID, errorId)
    }

    fun checkEqual(firstValue: Any, second: Any, @StringRes errID: Int) {
        if (firstValue != second) showError(errorID = errID)
    }

    fun checkGender(firstValue: Any, second: Any, @StringRes errID: Int) {
        if (firstValue == second) showError(errorID = errID)
    }

    fun checkPhone(phone: String,@IdRes viewID: Int? = null){
        checkEmpty(phone, R.string.error_blank_phone, viewID)
        checkLength(phone.trim().convertPhoneToNormalFormat(), 10,R.string.error_type_phone_not_enough, viewID)
    }
}