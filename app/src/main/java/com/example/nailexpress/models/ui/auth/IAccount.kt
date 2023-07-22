package com.example.nailexpress.models.ui.auth

import android.os.Parcelable
import com.example.nailexpress.R
import com.example.nailexpress.exception.resourceError
import com.example.nailexpress.exception.viewErrorCustom
import com.example.nailexpress.extension.convertPhoneToNormalFormat
import kotlinx.parcelize.Parcelize

interface IAccount {
}

@Parcelize
class LoginForm(
    var phone: String = "",
    var phone_code: String = "",
    var password: String = "",
    var device_token: String = "fNJJ3zrleSFK9NNsJ0_lZw:APA91bGnhK6zW2mvN4dUVR1qLgGmbMQG8p8ZTr5W8Lp--bYYsg43BHJbPpAmzDq6b1KtI0SLwU6Xz3YBYXvQdGNcmZTLDNhzkdbx0GR9NREiI3Znz4nch5FqWHSrpcylnUT9xa8o539i",
) : Parcelable {
    fun handle(): LoginForm {
        phone = phone.convertPhoneToNormalFormat()
        return this
    }

    fun validate() {
        if (phone.trim().isEmpty()) {
            resourceError(R.string.error_blank_phone)
        }
        if (phone.trim().convertPhoneToNormalFormat().length < 10) {
            resourceError(R.string.error_type_phone_not_enough)
        }
        if (password.isBlank()) viewErrorCustom(R.id.edtPassword, R.string.error_blank_password)
    }
}