package com.example.nailexpress.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.*
import com.example.nailexpress.R
import com.example.nailexpress.databinding.CustomEditTextBinding
import com.example.nailexpress.exception.IViewCustomerErrorHandler
import com.example.nailexpress.extension.*


enum class InputType(val index: Int) {
    NUMBER(0),
    DECIMAL(1);

    companion object {
        fun getTypeByIndex(index: Int): InputType {
            InputType.values().forEach {
                if (it.index == index) return it
            }
            return NUMBER
        }
    }
}

@BindingMethods(
    BindingMethod(
        type = CustomEditText::class,
        attribute = "text",
        method = "setText"
    ),
    BindingMethod(
        type = CustomEditText::class,
        attribute = "textAttrChanged",
        method = "setTextChangeListener"
    )

)

@InverseBindingMethods(
    InverseBindingMethod(
        type = CustomEditText::class,
        attribute = "text",
        method = "getText"
    )
)

class CustomEditText(context: Context, attributeSet: AttributeSet) : IViewCustomerErrorHandler,
    ConstraintLayout(context, attributeSet) {

    var text: String = ""
        get() = binding.etContent.text.toString()
        set(value) {
            field = value
            if (value == "-1") binding.etContent.clearText()
            else
                binding.etContent.setText(value)
        }


    private val binding by lazy {
        CustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)
    }


    var inputType: InputType = InputType.NUMBER
        set(value) {
            field = value
            when (value) {
                InputType.NUMBER -> {
                    binding.etContent.inputType = android.text.InputType.TYPE_CLASS_NUMBER
                }
                InputType.DECIMAL -> {
                    binding.etContent.inputType =
                        android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                    binding.etContent.setInputSignedDecimal(10, 2)
                }
            }
        }

    var maxNumberInput: Double = -1.0
        set(value) {
            field = value
            if (maxNumberInput == -1.0) {
                return
            }
            binding.etContent.addFilterLimitMaxNumber(value)
        }

    var hint = DEFAULT_STRING
    private var startText = ""
        set(value) {
            binding.startText.show(
                value.isNotEmpty()
            )
            binding.startText.text = value
            field = value
        }
    private var endText = ""
        set(value) {
            binding.endText.show(
                value.isNotEmpty()
            )
            binding.endText.text = value
            field = value
        }

    init {
        context.loadAttrs(attributeSet, R.styleable.CustomEditText) {
            hint = it.getString(R.styleable.CustomEditText_android_hint) ?: DEFAULT_STRING
            startText = it.getString(R.styleable.CustomEditText_custom_start_text) ?: DEFAULT_STRING
            endText = it.getString(R.styleable.CustomEditText_custom_end_text) ?: DEFAULT_STRING
            inputType =
                InputType.getTypeByIndex(it.getInt(R.styleable.CustomEditText_input_type, 0))
            maxNumberInput =
                it.getFloat(R.styleable.CustomEditText_max_number_input, -1f).toDouble()
        }

        setupListener()
        binding.etContent.hint = hint
    }


    fun setTextChangeListener(listener: InverseBindingListener) {
        binding.apply {
            etContent.doOnTextChanged { text, start, before, count -> listener.onChange() }
        }
    }

    private fun setupListener() {
        with(binding) {
            root.onClick {
                etContent.showKeyboard()
            }
        }
    }

    companion object {
        const val DEFAULT_STRING = ""
    }

    override fun handleError(errorMessage: Int) {
        binding.etContent.displayErrorAndFocus(errorMessage)
    }
}

private fun EditText.seekCursorToLast() {
    setSelection(length())
}
