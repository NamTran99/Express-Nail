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
import com.example.nailexpress.databinding.FragmentProfileBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.or1
import com.example.nailexpress.models.ui.main.User
import com.example.nailexpress.repository.AuthRepository
import com.example.nailexpress.repository.ProfileRepository
import com.example.nailexpress.views.ui.main.profile.adapters.AccountOption
import com.example.nailexpress.views.ui.main.profile.adapters.ProfileOptionAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileVM>(layoutId = R.layout.fragment_profile) {

    override val viewModel: ProfileVM by viewModels()

    override fun initView() {
        binding.apply {
            action = viewModel
        }
    }
}


@HiltViewModel
class ProfileVM @Inject constructor(
    app: Application, private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) :
    BaseViewModel(app), IActionTopBar by ActionTopBarImpl() {

    init {
        initTopBarAction(this)
    }

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.account))
    override val isShowBackButton: MutableLiveData<Boolean>
        get() = MutableLiveData(false)

    val adapter: ProfileOptionAdapter by lazy { ProfileOptionAdapter() }
    val user: MutableLiveData<User> = MutableLiveData(User())
    val role: MutableLiveData<String> = MutableLiveData("")

    val listOption = listOf(
        AccountOption(R.string.title_account_profile) {
            navigateToDestination(R.id.action_global_accountInforFragment)
        },
        AccountOption(R.string.my_salon) {
            navigateToDestination(R.id.action_navDashBoard_to_mySalonFragment)
        },
        AccountOption(R.string.change_pass) {
            navigateToDestination(R.id.action_global_changePassFragment)
        },
        AccountOption(R.string.message) {

        },
        AccountOption(R.string.about_app) {

        },
    )

    init {
        adapter.submit(listOption)
        getUserLocal()
        getRole()
    }

    fun onClickDeleteAccount() {
        // TO DO
    }

    fun onClickLogOut() = launch {
        authRepository.logOut()
        navigateToDestination(R.id.action_global_authGraph, popUpToDes = R.id.customerGraph, inclusive = true)
    }

    fun onChangeRole() {
        navigateToDestination(R.id.action_global_selectRoleProfileFragment)
    }

    private fun getRole() {
        role.value =
            getString((profileRepository.getRole() == AppConfig.AppRole.Customer) or1 R.string.role_customer or2 R.string.role_staff)
    }

    private fun getUserLocal() {
        user.value = profileRepository.getProfileDTO()
    }
}
