package com.example.nailexpress.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


fun Context.colorResource(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable {
    return ContextCompat.getDrawable(this, drawableRes)
        ?: error("Could not get drawable $drawableRes")
}

fun Context.colorStateList(colorRes: Int): ColorStateList {
    return ColorStateList.valueOf(colorResource(colorRes))
}