package com.example.nailexpress.functional

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern


class DecimalDigitsInputFilter(digitsBeforeZero : Int, digitsAfterZero : Int) : InputFilter {
    private  var mPattern: Pattern  = Pattern.compile("^[-]?(([1-9]{1}[0-9]{0," + (digitsBeforeZero - 1) + "})?||[0]{1})((\\.[0-9]{0," + digitsAfterZero + "})?)||(\\.)?");
    var enableDot  = digitsAfterZero != 0

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        destination: Spanned?,
        destinationStart: Int,
        destinationEnd: Int
    ): CharSequence ?{
        if(!enableDot && source == ".") return ""
        var newString: String =
            destination.toString().substring(0, destinationStart) + destination.toString()
                .substring(destinationEnd, destination.toString().length)

        newString =
            newString.substring(0, destinationStart) + source.toString() + newString.substring(
                destinationStart,
                newString.length
            )

        val matcher: Matcher = mPattern.matcher(newString)

        return if (matcher.matches()) {
            null
        } else ""
    }
}