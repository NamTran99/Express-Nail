package com.example.nailexpress.views.widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.core.view.viewBinding
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.nailexpress.databinding.PartialOtpViewBinding
import com.example.nailexpress.extension.applyTo
import com.example.nailexpress.extension.gone
import com.example.nailexpress.extension.invisible
import com.example.nailexpress.extension.show
import java.util.regex.Pattern

class OTPView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding = viewBinding(PartialOtpViewBinding::inflate)

    private val textViews = mutableListOf<TextView>()
    private val filter: InputFilter
        get() = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Pattern.compile(PATTERN)
                        .matcher(source[i].toString())
                        .matches()
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
    var onActiveChangeListener: (Boolean) -> Unit = {}
        set(value) {
            field = value
            value(mActive)
        }
    private var mActive = false
    val otp: String?
        get() = viewBinding.edtOtp.text?.toString()

    init {
        textViews.add(viewBinding.txt1)
        textViews.add(viewBinding.txt2)
        textViews.add(viewBinding.txt3)
        textViews.add(viewBinding.txt4)
        textViews.add(viewBinding.txt5)
        textViews.add(viewBinding.txt6)

        viewBinding.edtOtp.run {
            filters = arrayOf(filter, InputFilter.LengthFilter(6))
            doOnTextChanged { text, _, _, _ ->
                setOTP(text ?: "")
                val textLength = text?.length ?: 0
                setFocus(textLength)
                notifyActiveChangeIfNeeded(textLength)
            }
        }
    }

    private fun notifyActiveChangeIfNeeded(textLength: Int) {
        if (mActive && textLength < OTP_LENGTH) {
            mActive = false
            onActiveChangeListener(mActive)
        } else if (!mActive && textLength == OTP_LENGTH) {
            mActive = true
            onActiveChangeListener(mActive)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(l: OnTouchListener) {
        super.setOnTouchListener(l)
        viewBinding.edtOtp.setOnTouchListener(l)
    }

    override fun clearFocus() {
        super.clearFocus()
        hideKeyboard()
    }

    fun requestFocusOTP() {
        viewBinding.edtOtp.requestFocus()
        setFocus(0)
        showKeyboard()
    }

    fun clearFocusOTP() {
        textViews.applyTo { isActivated = false }
    }

    fun showLoading(boolean: Boolean) {
        if (boolean) {
            viewBinding.groupLoading.show()
            setError(null)
        } else {
            viewBinding.groupLoading.invisible()
        }
    }

    fun hideErrorAndLoading() {
        viewBinding.groupLoading.gone()
        viewBinding.tvError.gone()
    }

    fun setError(error: String?) {
        viewBinding.tvError.show(error != null) { text = error }
        textViews.forEach { it.isSelected = error != null }
    }

    fun setEnable(enable: Boolean) {
        if (enable) reset()
        viewBinding.edtOtp.run {
            setEnable(enable)
            requestFocus()
        }
    }

    fun reset() {
        viewBinding.edtOtp.setText("")
        textViews.forEach { it.isSelected = false }
    }

    private fun setFocus(length: Int) {
        textViews.forEachIndexed { index, textView ->
            textView.isActivated = index == length
        }
        if (length == textViews.size) {
            textViews.last().isActivated = true
        }
    }

    private fun setOTP(text: CharSequence) {
        textViews.forEachIndexed { index, textView ->
            if (index < text.length) {
                textView.text = text[index].toString()
            } else {
                textView.text = ""
            }
            textView.isEnabled = index >= text.length
        }
    }

    private fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(viewBinding.edtOtp, InputMethodManager.SHOW_FORCED)
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(viewBinding.edtOtp.windowToken, 0)
    }

    companion object {
        private const val OTP_LENGTH = 6
        private const val PATTERN = "[1234567890]*"
    }
}

fun EditText.setEnable(enable: Boolean) {
    isEnabled = enable
    isFocusable = enable
    isFocusableInTouchMode = enable
    alpha = if(enable){
        1f
    }else 0.6f
}

class OTPEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs) {

    init {
        isCursorVisible = false
        setTextColor(ContextCompat.getColor(context, android.R.color.transparent))
        setBackgroundResource(0)
        inputType = InputType.TYPE_CLASS_NUMBER
        setSelectAllOnFocus(false)
        setTextIsSelectable(false)
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        val text = text
        text?.let {
            if (selStart != text.length || selEnd != text.length) {
                setSelection(text.length, text.length)
                return
            }
        }
        super.onSelectionChanged(selStart, selEnd)
    }
}