package com.example.nailexpress.functional

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference

class UsPhoneNumberFormatter(private val mWeakEditText: WeakReference<EditText>) : TextWatcher {

    private var mFormatting: Boolean = false
    private var clearFlag: Boolean = false
    private var mLastStartLocation: Int = 0
    private var mLastBeforeText: String? = null

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                   after: Int) {
        if (after == 0 && s.toString() == "1 ") {
            clearFlag = true
        }
        mLastStartLocation = start
        mLastBeforeText = s.toString()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (!mFormatting) {
            mFormatting = true
            val curPos = mLastStartLocation
            val beforeValue = mLastBeforeText
            val currentValue = s.toString()
            val formattedValue = formatUsNumber(s)
            if (currentValue.length > beforeValue!!.length) {
                val setCusorPos = formattedValue.length - (beforeValue.length - curPos)
                mWeakEditText.get()!!.setSelection(if (setCusorPos < 0) 0 else setCusorPos)
                mWeakEditText.get()!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(14))
            } else {
                var setCusorPos = formattedValue.length - (currentValue.length - curPos)
                if (setCusorPos > 0 && !Character.isDigit(formattedValue[setCusorPos - 1])) {
                    setCusorPos--
                }
                mWeakEditText.get()!!.setSelection(if (setCusorPos < 0) 0 else setCusorPos)
            }
            mFormatting = false
        }
    }

    private fun formatUsNumber(text: Editable): String {
        val formattedString = StringBuilder()
        var p = 0
        while (p < text.length) {
            val ch = text[p]
            if (!Character.isDigit(ch)) {
                text.delete(p, p + 1)
            } else {
                p++
            }
        }
        val allDigitString = text.toString()
        val totalDigitCount = allDigitString.length
        if (totalDigitCount == 0
            || totalDigitCount > 10 && !allDigitString.startsWith("1")
            || totalDigitCount > 11
        ) {
            text.clear()
            text.append(allDigitString)
            return allDigitString
        }
        var alreadyPlacedDigitCount = 0
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append("("
                    + allDigitString.substring(alreadyPlacedDigitCount,
                alreadyPlacedDigitCount + 3) + ") ")
            alreadyPlacedDigitCount += 3
        }
        if (totalDigitCount - alreadyPlacedDigitCount > 3) {
            formattedString.append(allDigitString.substring(
                alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3) + "-")
            alreadyPlacedDigitCount += 3
        }
        if (totalDigitCount > alreadyPlacedDigitCount) {
            formattedString.append(allDigitString
                .substring(alreadyPlacedDigitCount))
        }

        text.clear()
        text.append(formattedString.toString())
        return formattedString.toString()
    }
}