package com.example.nailexpress.views.ui.authentication

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentSelectRoleBinding
import com.example.nailexpress.databinding.FragmentSignUpBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.extension.inputTypePhoneUS
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.AuthRepository
import com.example.nailexpress.repository.ProfileRepository
import com.example.nailexpress.views.ui.authentication.verifyPhone.VerifyPhoneVM
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SelectRoleFragment :
    BaseFragment<FragmentSelectRoleBinding, SelectAppRoleVM>(layoutId = R.layout.fragment_select_role) {

    override val viewModel: SelectAppRoleVM by viewModels()

     override fun initView() {
        binding.apply {
            action = viewModel
        }
    }
}
@HiltViewModel
class SelectAppRoleVM @Inject constructor(app: Application, private val profileRepository: ProfileRepository) :  BaseViewModel(app){
    fun setRole(appRole: AppConfig.AppRole)= launch{
        profileRepository.selectRole(appRole)
//        if(appRole == AppConfig.AppRole.Customer){
//            navigateToDestination(R.id.customerGraph)
//        }else{
//
//        }
        navigateToDestination(R.id.action_global_customerGraph)
    }
}

