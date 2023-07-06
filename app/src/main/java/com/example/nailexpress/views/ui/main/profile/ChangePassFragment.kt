package com.example.nailexpress.views.ui.main.profile

import android.app.Application
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.support.core.livedata.changeValue
import android.support.core.livedata.map
import android.support.core.livedata.refresh
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentAccountInforBinding
import com.example.nailexpress.extension.convertToRequest
import com.example.nailexpress.extension.convertToResult
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.User
import com.example.nailexpress.repository.AuthRepository
import com.example.nailexpress.repository.ProfileRepository
import com.example.nailexpress.views.ui.main.customer.salon.CreateSalonFragment
import com.example.nailexpress.views.widgets.CustomEditTextProfile
import com.google.android.libraries.places.api.model.Place
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.nailexpress.app.AppConfig.Status
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
    app: Application, private val profileRepository: ProfileRepository,
) :
    BaseViewModel(app), IActionTopBar {
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
