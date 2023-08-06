package com.example.nailexpress.extension

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.utils.FileUtils.getPath
import com.example.nailexpress.views.MainActivity

fun Context.getFilePath(it: Uri) = getPath(this, it)

fun BaseFragment<*, *>.copyText(text: String) {
    val clipboard: ClipboardManager? =
        this.requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("nails:text:copy", text)
    clipboard?.setPrimaryClip(clip)
    this.success("Copied!")
}

fun Context.asMainActivity() = when (this) {
    is BaseActivity<*> -> this
    is FragmentActivity -> this as? BaseActivity<*>
    else -> null
}

fun Context.getColor(res: Int) = ContextCompat.getColor(this,res)

