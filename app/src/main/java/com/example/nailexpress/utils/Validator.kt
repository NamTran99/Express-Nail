package com.example.nailexpress.utils;

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import com.example.nailexpress.R
import com.example.nailexpress.exception.resourceError
import java.util.regex.Pattern


object Validator {
    fun checkEmpty(text: String, @StringRes id: Int){
        if(text.trim().isBlank()) resourceError(id)
    }

    fun checkLength(text: String, length: Int,@StringRes id: Int){
        if(text.trim().length < length) resourceError(id)
    }


}


