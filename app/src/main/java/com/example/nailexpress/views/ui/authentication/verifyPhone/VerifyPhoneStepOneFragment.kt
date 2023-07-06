package com.example.nailexpress.views.ui.authentication.verifyPhone

import android.app.Application
import android.os.Bundle
import android.support.core.event.ErrorEvent
import android.support.core.event.LoadingEvent
import android.support.core.livedata.ErrorLiveData
import android.support.core.livedata.LoadingLiveData
import android.support.core.livedata.post
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentVerifyStepOneBinding
import com.example.nailexpress.extension.*
import com.example.nailexpress.models.ui.auth.VerifyForm
import com.example.nailexpress.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class VerifyPhoneStepOneFragment :
    BaseFragment<FragmentVerifyStepOneBinding, VerifyPhoneVM>(layoutId = R.layout.fragment_verify_step_one) {

    override val viewModel: VerifyPhoneVM by activityViewModels()

    override fun initView() {
        binding.apply {
            action = viewModel
            edtPhone.inputTypePhoneUS()
            spnCountry.configSpinner(
                false,
                contentID = R.array.ar_phone_country
            )
        }
    }
}

@HiltViewModel
class VerifyPhoneVM @Inject constructor(app: Application, private val authRepository: AuthRepository) :
    BaseViewModel(app) {
    companion object {
        const val SPIN_EN = 0
        const val SPIN_VN = 1

        const val SPIN_EN_CODE = "+1"
        const val SPIN_VN_CODE = "+84"
    }

    val errorCustom: ErrorEvent = ErrorLiveData()
    val loadingCustom: LoadingEvent = LoadingLiveData()
    val resendCounting = MutableLiveData<ResendState>(ResendState.Initialize)

    val spinIndexSelected = MutableLiveData(SPIN_EN)

    val verifyForm = MediatorLiveData<VerifyForm>(VerifyForm()).apply {
        addSource(spinIndexSelected) {
            this.value?.phone_code = if (it == SPIN_EN) SPIN_EN_CODE else SPIN_VN_CODE
        }
    }

    fun setResending() = launch {
        val phoneNumber = verifyForm.value?.phone?.convertPhoneToNormalFormat()
        resendCounting.post(ResendState.Sending)
        val exception = runCatching {
//            requestOTPRepo(phoneNumber)
        }
            .exceptionOrNull()
        countToRequestResend()
        if (exception != null) throw exception
    }
    fun countToRequestResend() = launch(null) {
        val timeout = AppConfig.otpTimeout
        var remain = timeout
        val counting = ResendState.Counting(remain.toString())
        while (remain > 0) {
            resendCounting.post(counting.copy(remain = remain.toString()))
            remain -= 1
            delay(1000)
        }
        resendCounting.post(ResendState.ReadyToResend)
    }


    fun verifyPhone() = launch {
        authRepository.verifyPhone(
            verifyForm.value!!.handle()
        )
        navigateToDestination(R.id.action_verifyFragmentStepOne_to_verifyPhoneStepTwoFragment)
    }
    fun register() = launch {
        authRepository.register(
            verifyForm.value!!.handle()
        )
        navigateToDestination(R.id.action_signUpFragment_to_selectRoleFragment)
    }


    fun verifyCode(code: String?) = launch(loadingCustom, errorCustom) {
        verifyForm.value?.code = code
        authRepository.verifyCode(
            verifyForm.value!!.handle()
        )
        navigateToDestination(R.id.action_verifyPhoneStepTwoFragment_to_signUpFragment)
    }
}

sealed class ResendState {
    object Initialize : ResendState()
    data class Counting(val remain: String) : ResendState()
    object ReadyToResend : ResendState()
    object Sending : ResendState()
}
