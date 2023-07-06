package com.example.nailexpress.views.ui.authentication.verifyPhone.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.Observer
import com.example.nailexpress.R
import com.example.nailexpress.databinding.FragmentVerifyStepTwoBinding
import com.example.nailexpress.extension.gone
import com.example.nailexpress.extension.hide
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.show
import com.example.nailexpress.views.ui.authentication.verifyPhone.ResendState
import com.example.nailexpress.views.ui.authentication.verifyPhone.VerifyPhoneStepTwoFragment

class ResendOTPAdapter (
    private val binding:  FragmentVerifyStepTwoBinding,
    private val onResendListener: () -> Unit
) : Observer<ResendState> {

    @SuppressLint("ResourceAsColor")
    override fun onChanged(t: ResendState) {
        val txtResend = binding.txtOTPResend
        when (t) {
            ResendState.Initialize -> txtResend.gone()
            ResendState.ReadyToResend -> {
                binding.apply {
                   btResendCode.show()
                    txtResend.hide()
                    btResendCode.onClick {
                        onResendListener()
                    }
                }
                txtResend.isActivated = true
                txtResend.isEnabled = true
            }
            is ResendState.Counting -> {
                binding.apply {
                    txtResend.show()
                    btResendCode.hide()
                }
                txtResend.text =
                    getString(R.string.auth_otp_resend_in_remaining_s, t.remain)
                txtResend.onClick(null)
                txtResend.isActivated = false
                txtResend.isEnabled = false
            }
            is ResendState.Sending -> {
                txtResend.text = getString(R.string.auth_otp_sending)
                txtResend.onClick(null)
                txtResend.isActivated = false
                txtResend.isEnabled = false
            }
        }
    }

    private fun getString(resId: Int, vararg args: String): String {
        return binding.root.resources.getString(resId, *args)
    }
}