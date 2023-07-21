package com.example.nailexpress.views.ui.main.profile

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.ProfileRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.databinding.FragmentChangePassBinding
import com.example.nailexpress.models.ui.main.PasswordForm


@AndroidEntryPoint
class ChangePassFragment :
    BaseFragment<FragmentChangePassBinding, ChangePassVM>(layoutId = R.layout.fragment_change_pass) {

    override val viewModel: ChangePassVM by viewModels()

    override fun initView() {
        binding.apply {
            action = viewModel
        }
    }
}


@HiltViewModel
class ChangePassVM @Inject constructor(
   val app: Application, private val profileRepository: ProfileRepository,
) :    BaseViewModel(app), IActionTopBar by ActionTopBarImpl() {

    init {
        initTopBarAction(this)
    }
    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.change_pass))

    val passForm = MutableLiveData(PasswordForm())


    fun onClickChangePass() = launch{
        passForm.value?.apply {
            profileRepository.changePass(this)
            showToast(R.string.success_change_pass)
            backScreen()
        }
    }
}
