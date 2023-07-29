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
import com.example.nailexpress.views.ui.main.profile.adapters.ISelectRoleAdapterCallBack
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
) : BaseViewModel(app), IActionTopBar by ActionTopBarImpl(), ISelectRoleAdapterCallBack {

    val adapter: SelectRoleAdapter by lazy { SelectRoleAdapter(this) }
    val isChangeRole = MutableLiveData(false)
    var currentRole = AppConfig.AppRole.Customer

    init {
        initTopBarAction(this)
        setTitle(R.string.title_select_role)
        getRole()
    }

    override val onItemSelect: (appRole: AppConfig.AppRole) -> Unit = { appRole ->
        isChangeRole.value = appRole != currentRole
    }

    private fun getListData(role: AppConfig.AppRole) = listOf<RoleOption>(
        RoleOption(
            R.string.role_staff,
            appRole = AppConfig.AppRole.Staff,
        ).setIsCheck(role),
        RoleOption(
            R.string.role_customer,
            appRole = AppConfig.AppRole.Customer,
        ).setIsCheck(role)
    )

    private fun getRole() = launch {
        (profileRepository.getRole() ?: AppConfig.AppRole.Customer).let {
            currentRole = it
            adapter.submit(getListData(it))
        }
    }

    fun setRole() = launch {
        adapter.mitems.find { it.isCheck }?.appRole?.let {
            if (profileRepository.selectRole(it)) {
                if (it == AppConfig.AppRole.Customer) {
                    navigateToDestination(R.id.action_selectRoleProfileFragment_to_customerGraph, popUpToDes = R.id.staffGraph, inclusive = true )
                } else navigateToDestination(R.id.action_selectRoleProfileFragment_to_staffGraph, popUpToDes = R.id.customerGraph, inclusive = true)
            }
        }
    }
}

