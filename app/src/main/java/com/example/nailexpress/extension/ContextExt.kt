package com.example.nailexpress.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.*
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.utils.FileUtils.getPath

fun Context.getFilePath(it: Uri) = getPath(this, it)

fun BaseFragment<*,*>.copyText(text: String) {
    val clipboard: ClipboardManager? =
        this.requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("nails:text:copy", text)
    clipboard?.setPrimaryClip(clip)
    this.success("Copied!")
}


