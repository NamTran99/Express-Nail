package com.example.nailexpress.views.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentSignUpBinding
import com.example.nailexpress.extension.inputTypePhoneUS
import com.example.nailexpress.views.ui.authentication.verifyPhone.VerifyPhoneVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, VerifyPhoneVM>(layoutId = R.layout.fragment_sign_up) {

    override val viewModel: VerifyPhoneVM by activityViewModels()


    override fun initView() {
        binding.apply {
            action = viewModel
        }
    }
}

