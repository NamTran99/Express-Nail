package com.example.nailexpress.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.*
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.databinding.CustomEditTextProfileBinding
import com.example.nailexpress.extension.*
import com.example.nailexpress.app.AppConfig.Status

@BindingMethods(
    BindingMethod(
        type = CustomEditTextProfile::class,
        attribute = "status",
        method = "setStatus"
    ),

    BindingMethod(
        type = CustomEditTextProfile::class,
        attribute = "text",
        method = "setText"
    ),

    BindingMethod(
        type = CustomEditTextProfile::class,
        attribute = "text",
        method = "setText"
    ),

    BindingMethod(
        type = CustomEditTextProfile::class,
        attribute = "textAttrChanged",
        method = "setTextChangeListener"
    )
)

@InverseBindingMethods(
    InverseBindingMethod(
        type = CustomEditTextProfile::class,
        attribute = "text",
        method = "getText"
    )
)

class CustomEditTextProfile(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding by lazy {
        CustomEditTextProfileBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var isPhoneType = false
        set(value) {
            if (value) {
                binding.etContent.inputTypePhoneUS()
                binding.etContent.inputType = android.text.InputType.TYPE_CLASS_PHONE
            }
            field = value
        }

    var onClickView : (() -> Unit) = {}

    var status: AppConfig.Status = Status.READ
        set(value) {
            field = value
            binding.etContent.isFocusableInTouchMode = (value == Status.UPDATE && isfocusable)
        }

    var text: String = ""
        get() = binding.etContent.text.toString()
        set(value) {
            field = value
            binding.etContent.setText(value)
        }

    private var hint = ""
        set(value) {
            binding.etHint.text = value
            field = value
        }

    private var isfocusable = false

    private var src:Int = 0
        set(value) {
            binding.imgImage.setImageResource(value)
            field = value
        }

    init {
        context.loadAttrs(attributeSet, R.styleable.CustomEditTextProfile) {
            src = it.getResourceId(R.styleable.CustomEditTextProfile_image_custom, 0)
            hint = it.getString(R.styleable.CustomEditTextProfile_android_hint) ?: DEFAULT_STRING
            isPhoneType = it.getBoolean(R.styleable.CustomEditTextProfile_isPhoneType, false)
            isfocusable = it.getBoolean(R.styleable.CustomEditTextProfile_is_focusable, true)
        }
        setupListener()
    }

    fun setTextChangeListener(listener: InverseBindingListener) {
        binding.apply {
            etContent.doOnTextChanged { text, start, before, count -> listener.onChange() }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        with(binding) {
            etContent.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (status == Status.UPDATE) {
                    etHint.show(!hasFocus)
                }
            }
            
            root.setOnTouchListener { v, motionEvent ->
                if(motionEvent.action == MotionEvent.ACTION_DOWN && status == Status.UPDATE) {
                    onClickView.invoke()
                    etContent.showKeyboard()
                }
                false
            }
        }
    }

    companion object {
        const val DEFAULT_STRING = ""
    }
}

