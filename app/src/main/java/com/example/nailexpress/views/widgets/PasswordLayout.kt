package com.example.nailexpress.views.widgets

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.*
import com.example.nailexpress.R
import com.example.nailexpress.databinding.EdtPasswordInputBinding
import com.example.nailexpress.exception.IViewCustomerErrorHandler
import com.example.nailexpress.extension.displayErrorAndFocus
import com.example.nailexpress.extension.getTextString
import com.example.nailexpress.extension.loadAttrs


class PasswordLayout(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet), IViewCustomerErrorHandler {

    var text: String = ""
        get() = binding.edtPassword.text.toString()
        set(value) {
            binding.edtPassword.setText(value)
            field = value
        }

    fun getEdtView() = binding.edtPassword


    val binding by lazy {
        EdtPasswordInputBinding.inflate(LayoutInflater.from(context), this, true)
    }

    var hint = DEFAULT_STRING
    var drawableStart: Int = 0
        set(value) {
            binding.apply {
                edtPassword.setCompoundDrawablesWithIntrinsicBounds(value, 0, 0, 0)
            }
        }

    init {
        context.loadAttrs(attributeSet, R.styleable.PasswordLayout) {
            hint = it.getString(R.styleable.PasswordLayout_hint) ?: DEFAULT_STRING
            drawableStart = it.getResourceId(R.styleable.PasswordLayout_drawableStart, 0)
        }

        setupListener()
        binding.edtPassword.hint = hint
    }

    private fun setupListener() {
        with(binding) {
            txtPassActionShow.setOnClickListener {
                isActivated = !isActivated
                txtPassActionShow.text =
                    context.getString(if (isActivated) R.string.hide else R.string.show)
                edtPassword
                    .showPassword(isActivated)
                    .seekCursorToLast()
            }
        }
    }

    companion object {
        const val DEFAULT_STRING = ""

    }

    override fun handleError(errorMessage: Int) {
        binding.edtPassword.displayErrorAndFocus(errorMessage)
    }
}

private fun EditText.seekCursorToLast() {
    setSelection(length())
}

private fun EditText.showPassword(activated: Boolean): EditText {
    transformationMethod = if (!activated) PasswordTransformationMethod.getInstance()
    else HideReturnsTransformationMethod.getInstance()
    return this
}
