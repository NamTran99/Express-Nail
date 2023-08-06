package com.example.nailexpress.views.ui.authentication

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentLoginBinding
import com.example.nailexpress.datasource.local.PrefUtils
import com.example.nailexpress.extension.configSpinner
import com.example.nailexpress.extension.inputTypePhoneUS
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.ui.auth.LoginForm
import com.example.nailexpress.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginVM>(layoutId = R.layout.fragment_login) {

    override val viewModel: LoginVM by viewModels()

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
class LoginVM @Inject constructor(app: Application, private val authRepository: AuthRepository) :
    BaseViewModel(app) {

    private val prefUtils by lazy { PrefUtils(app.applicationContext) }

    companion object {
        const val SPIN_EN = 0
        const val SPIN_VN = 1

        const val SPIN_EN_CODE = "+1"
        const val SPIN_VN_CODE = "+84"
    }

    val spinIndexSelected = MutableLiveData(SPIN_EN)

    val loginForm = MediatorLiveData<LoginForm>(
        LoginForm(
            phone_code = SPIN_VN_CODE,
            device_token = prefUtils.getDeviceId() ?: ""
        )
    ).apply {
        addSource(spinIndexSelected) {
            this.value?.phone_code = if (it == SPIN_EN) SPIN_EN_CODE else SPIN_VN_CODE
        }
    }

    fun login() = launch {
        authRepository.loginAccount(
            loginForm.value!!.handle()
        )
        navigateToDestination(R.id.action_loginFragment_to_selectRoleFragment)
    } // goi api

    fun register() = launch {
        navigateToDestination(R.id.action_loginFragment_to_verifyFragment)
    }
}