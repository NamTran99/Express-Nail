package com.example.nailexpress.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ListPopupWindow
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil.setContentView
import androidx.viewbinding.ViewBinding
import com.example.nailexpress.R
import com.example.nailexpress.extension.setMargins
import com.example.nailexpress.extension.showKeyboard
import dagger.hilt.android.migration.CustomInjection.inject

open class BaseDialog(context: Context) : Dialog(context) {
    var activityContext:Context = context
//    val appEvent by inject<AppEvent>()
    @JvmName("getActivityContext1")
    fun getActivityContext() = context
    // unfocus edittext when select outside
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        dispatchTouchEventCustom(event)
        return super.dispatchTouchEvent(event)
    }

    open fun dispatchTouchEventCustom(event:MotionEvent){
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.x.toInt(), event.y.toInt())) {
                    v.clearFocus()
                    v.showKeyboard(false)
                }
            }
        }
    }

    var isSlideShow: Boolean = false
        set(value) {
            field = value
            if (value) window!!.attributes.windowAnimations = R.style.AppTheme_DialogAnimation
        }

    @SuppressLint("SuspiciousIndentation")
    fun <T : ViewBinding> viewBinding(
        function: (context: LayoutInflater) -> T,
        isMargin: Boolean = true
    ): T {
        val binding = function(layoutInflater)
        val container = FrameLayout(context)
        setContentView(container)
        if (isMargin)
        container.setMargins(R.dimen.size_20)
        container.setBackgroundResource(R.drawable.bg_white_radius)
        container.addView(binding.root)
        return binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setLayout(ListPopupWindow.MATCH_PARENT, ListPopupWindow.WRAP_CONTENT)
        requestTransparent()
    }

    private fun requestTransparent() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setBackGroundBLur(){
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.setDimAmount(0.5f)
    }

    fun removeDim() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}