package com.example.nailexpress.views.dialog.picker

import android.app.DatePickerDialog
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.extension.toDateWithFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerDialog(private val activity: BaseActivity<*>) :
    DatePickerDialog(activity, R.style.DialogTheme, null, 0, 0, 0),
    DatePicker.OnDateChangedListener {
    private var mView: View? = null
    private var mDisableFutureDate = false
    private var mDisablePastDate = true
    private var mNumberLimit: Int = 0
    var onDateListener: ((api: String) -> Unit)? = null

    init {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                dismiss()
                super.onDestroy(owner)
            }
        })
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    /**
     * Limit year old
     */
    fun setLimit(number: Int) {
        mNumberLimit = number
    }

    /**
     * Disable future date
     * default: true
     */

    var disPlayFormat = FORMAT_DATE_DISPLAY
    fun setDisplayFormat(form: String) {
        disPlayFormat = form
    }

    fun setDisableFutureDates(disableFutureDates: Boolean) {
        mDisableFutureDate = disableFutureDates
    }

    fun setDisablePastDates(disablePastDates: Boolean) {
        mDisablePastDate = disablePastDates
    }

    private var mEnableDisplay = true

    fun setupClickWithView(view: View, enableDisplay: Boolean = true) {
        mEnableDisplay = enableDisplay
        mView = view
        view.setOnClickListener {
            this@DatePickerDialog.handleChooseDate(getViewValue())
        }
        val valueCurrent = getViewValue()
        if (valueCurrent.equals(" ", ignoreCase = true)
            || valueCurrent.equals("", ignoreCase = true)
        )
            display(
                SimpleDateFormat(
                    FORMAT_DATE_DISPLAY,
                    Locale.getDefault()
                ).format(Calendar.getInstance().time),
                SimpleDateFormat(
                    FORMAT_DATE_API,
                    Locale.getDefault()
                ).format(Calendar.getInstance().time)
            )
    }

    private var positiveText: Int? = null
    fun setPositiveButtonText(text: Int) {
        positiveText = text
    }

    private fun handleChooseDate(timeNow: String) {
        var timeNowL = timeNow
        val calendar = Calendar.getInstance()

        if (timeNowL.equals(" ", ignoreCase = true) || timeNowL.equals("", ignoreCase = true))
            timeNowL = EMPTY_BIRTHDAY
        val year =
            if (timeNowL == EMPTY_BIRTHDAY || !timeNowL.contains("-")) calendar.get(Calendar.YEAR) else timeNow.toDateWithFormat(
                formatInput = FORMAT_DATE_API,
                formatOutput = YEAR
            )
        val day =
            if (timeNowL == EMPTY_BIRTHDAY || !timeNowL.contains("-")) calendar.get(Calendar.DATE) else timeNow.toDateWithFormat(
                formatInput = FORMAT_DATE_API,
                formatOutput = DAY
            )
        val month =
            if (timeNowL == EMPTY_BIRTHDAY || !timeNowL.contains("-")) calendar.get(Calendar.MONTH) else timeNow.toDateWithFormat(
                formatInput = FORMAT_DATE_API,
                formatOutput = MONTH
            ) - 1

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.AppDatePickerCalendarDialog, { _, year1, monthOfYear, dayOfMonth ->
                calendar.clear()
                calendar.set(year1, monthOfYear, dayOfMonth, 0, 0, 0)
                val simpleDateFormat = SimpleDateFormat(disPlayFormat, Locale.getDefault())
                val simpleDateTagFormat = SimpleDateFormat(FORMAT_DATE_API, Locale.getDefault())
                display(
                    simpleDateFormat.format(calendar.time),
                    simpleDateTagFormat.format(calendar.time)
                )
                onDateListener?.invoke(simpleDateTagFormat.format(calendar.time))
            },
            year,
            month,
            day
        )

        applyConfig(datePickerDialog)
        datePickerDialog.show()
        datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.white)
        datePickerDialog.getButton(BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.color_primary))
        datePickerDialog.getButton(BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.gray))
        datePickerDialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        positiveText?.let {
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setText(it)
        }
    }

    private fun display(date: String, dateTag: String = "") {
        when (mView) {
            is TextView -> {
                if (mEnableDisplay) {
                    (mView as TextView).text = date
                }
                (mView as TextView).tag = dateTag
            }
        }
    }

    private fun getViewValue(): String {
        return try {
            when (mView) {
                is TextView -> (mView as TextView).tag.toString()
                else -> ""
            }
        } catch (ex: NullPointerException) {
            ""
        }
    }

    private fun applyConfig(datePickerDialog: DatePickerDialog) {
        if (mDisableFutureDate)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        if (mDisablePastDate)
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mDay = calendar.get(Calendar.DATE)
        val mMonth = calendar.get(Calendar.MONTH)

        val maxDate = Calendar.getInstance()
        maxDate.set(Calendar.DAY_OF_MONTH, mDay)
        maxDate.set(Calendar.MONTH, mMonth)
        maxDate.set(Calendar.YEAR, mYear - mNumberLimit)

        datePicker.maxDate = maxDate.timeInMillis
    }

    companion object {
        private const val MONTH = "MM"
        private const val DAY = "dd"
        private const val YEAR = "yyyy"
        private const val EMPTY_BIRTHDAY = ""
        const val FORMAT_DATE_API = "yyyy-MM-dd"
        const val FORMAT_DATE_DISPLAY = "MMM dd (EEEE)"
    }
}