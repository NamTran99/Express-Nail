package com.example.nailexpress.views.ui.authentication.verifyPhone

import android.text.SpannableStringBuilder
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentVerifyStepTwoBinding
import com.example.nailexpress.extension.formatPhoneUSCustom
import com.example.nailexpress.extension.lockTvButton
import com.example.nailexpress.views.ui.authentication.verifyPhone.adapter.ResendOTPAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyPhoneStepTwoFragment :
    BaseFragment<FragmentVerifyStepTwoBinding, VerifyPhoneVM>(layoutId = R.layout.fragment_verify_step_two) {

    override val viewModel: VerifyPhoneVM by activityViewModels()

    override fun initView() {
        binding.apply {
            action = viewModel
            viewModel.apply {
                errorCustom.bind { viewOTP.setError(it.message) }
                loadingCustom.bind { viewOTP.showLoading(it) }
                resendCounting.bind(ResendOTPAdapter(binding) {
                    viewModel.setResending()
                })

                viewOTP.onActiveChangeListener = { it lockTvButton btResendCode }
                viewOTP.requestFocusOTP()
                btVerify.setOnClickListener {
                    viewModel.apply {
                        verifyCode(viewOTP.otp)
                    }
                }

                val desPhone = SpannableStringBuilder().apply {
                    append(getString(R.string.verify_des_3))
                    append("\n\"")
                    color(requireContext().getColor(R.color.black)){
                        append(viewModel.getVerifyPhone().formatPhoneUSCustom())
                    }
                    append(""""""")
                }

                tvDesPhone.text = desPhone

                countToRequestResend()
            }
        }
    }
}

