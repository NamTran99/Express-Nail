package com.example.nailexpress.utils

import android.app.Dialog
import android.support.core.event.ErrorEvent
import android.support.core.event.LoadingEvent
import android.support.core.livedata.ErrorLiveData
import android.support.core.livedata.LoadingLiveData
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nailexpress.R

object ViewModelHandleUtils {
    val isLoading: LoadingEvent = LoadingLiveData()
    val isError: ErrorEvent = ErrorLiveData()
    private val TAG by lazy { "DialogUtils" }

    @Volatile
    private var dialog: Dialog? = null

    fun showLoading() {
        isLoading.post(true)
    }

    fun hideLoading() {
        isLoading.post(false)
    }

    fun showLoadingDialog(context: Any) {
        val activity = (context as? AppCompatActivity) ?: (context as Fragment).requireActivity()
        activity.let {
            if (it.isFinishing) return
            dialog = Dialog(it).apply {
                setContentView(R.layout.dialog_loading)
                window?.apply {
                    attributes = WindowManager.LayoutParams().apply {
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.MATCH_PARENT
                    }
                    setBackgroundDrawableResource(android.R.color.transparent)
                }
                setCancelable(false)
                show()
            }
        }
    }


    fun dismissLoadingDialog() {
        dialog?.dismiss()
    }

    fun clearLoading() {
        dialog?.dismiss()
        dialog = null
    }
}