package com.example.nailexpress.models.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class AppImage(
    val id: Int? = null,
    val image: String = ""
) : Parcelable