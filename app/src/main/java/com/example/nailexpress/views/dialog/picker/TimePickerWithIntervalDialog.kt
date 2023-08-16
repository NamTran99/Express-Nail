package com.example.nailexpress.views.dialog.picker

import android.content.Context
import android.support.core.view.ViewScopeOwner
import com.example.nailexpress.base.BaseDialog
import com.example.nailexpress.databinding.DialogTimePickerBinding
import com.example.nailexpress.extension.Format
import com.example.nailexpress.extension.convertTime
import com.example.nailexpress.extension.onClick

class TimePickerCustomDialog(context: Context) :
    BaseDialog(context) {
    private val binding = viewBinding(DialogTimePickerBinding::inflate)

//    var onSubmitClick: ((time: String) -> Unit) = { _ ->}
    // time: 24h format

    init {
        binding.apply {
            btCancel.onClick {
                dismiss()
            }
        }
    }

    fun show(time: String = "8:00 am", onSubmitClick: ((time_display: String, time_api: String) -> Unit)) {
        binding.apply {
            try {
                timePicker.setInitialSelectedTime(time)
            }catch (e:Exception){
                timePicker.setInitialSelectedTime("12:00 am")
            }
            btSetUp.onClick {
                getCurrentTime(onSubmitClick)
                dismiss()
            }
        }
        super.show()
    }

    private fun getCurrentTime(onSubmitClick: ((time_display: String, time_api: String) -> Unit)){
        binding.timePicker.getCurrentlySelectedTime().let{
            onSubmitClick.invoke(
                it,
                it.convertTime(Format.FORMAT_TIME_1, Format.FORMAT_TIME_2)
            )
        }
    }
}

interface TimePickerCustomDialogOwner : ViewScopeOwner {
    val timePickerDialog: TimePickerCustomDialog
        get() = with(viewScope) {
            getOr("timepicker:dialog") { TimePickerCustomDialog(context) }
        }
}