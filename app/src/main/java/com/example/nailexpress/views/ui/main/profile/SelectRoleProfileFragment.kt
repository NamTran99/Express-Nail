package com.example.nailexpress.views.ui.main.profile

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentSelectRoleProfileBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.ProfileRepository
import com.example.nailexpress.views.ui.main.profile.adapters.RoleOption
import com.example.nailexpress.views.ui.main.profile.adapters.SelectRoleAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SelectRoleProfileFragment :
    BaseFragment<FragmentSelectRoleProfileBinding, SelectAppRoleProfileVM>(layoutId = R.layout.fragment_select_role_profile) {

    override val viewModel: SelectAppRoleProfileVM by viewModels()

    override fun initView() {
        binding.apply {
            action = viewModel
        }
    }
}

@HiltViewModel
class SelectAppRoleProfileVM @Inject constructor(
    app: Application,
    private val profileRepository: ProfileRepository
) : BaseViewModel(app), IActionTopBar by ActionTopBarImpl() {

    val adapter: SelectRoleAdapter by lazy { SelectRoleAdapter() }
    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_create_salon))
    init {
        getRole()
    }

    private fun getListData(role: AppConfig.AppRole?) = listOf<RoleOption>(
        RoleOption(
            R.string.role_staff,
            appRole = AppConfig.AppRole.Staff,
            isCheck = role == AppConfig.AppRole.Staff
        ),
        RoleOption(
            R.string.role_customer,
            appRole = AppConfig.AppRole.Customer,
            isCheck = role == AppConfig.AppRole.Customer
        )
    )

    private fun getRole() = launch {
        adapter.submit(getListData(profileRepository.getRole()))
    }

    fun setRole() = launch {
        adapter.mitems.find { it.isCheck }?.appRole?.let {
            profileRepository.selectRole(it)
        }
    }
}

