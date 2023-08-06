package com.example.nailexpress.views.dialog

import android.view.Gravity
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialogFragment
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.DialogDeniedStatusBinding
import com.example.nailexpress.views.ui.main.staff.BookingOfMeFragment

class DialogDenied : BaseDialogFragment<DialogDeniedStatusBinding>() {
    override val gravity: Int = Gravity.CENTER_HORIZONTAL or Gravity.CENTER
    override val layoutId = R.layout.dialog_denied_status

    override fun initView() {
        with(binding) {
            btnSend.setOnClickListener {
                if (tvContent.text.toString().isNullOrBlank()) {
                    showToast(R.string.lbl_please_enter_a_reason_for_cancellation)
                } else {
                    (parentFragment as BaseFragment<*,*>).viewModel.let {
                        it.javaClass.getDeclaredMethod("changeStatusBookingDeniedAfterShowDialog",String::class.java).apply {
                            isAccessible = true
                            invoke(it,tvContent.text.toString())
                        }
                    }
                }
                dismiss()
            }

            tvClose.setOnClickListener {
                dismiss()
            }
        }
    }
}