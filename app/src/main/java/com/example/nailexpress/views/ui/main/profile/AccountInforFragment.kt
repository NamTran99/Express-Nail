package com.example.nailexpress.views.ui.main.profile

import android.app.Application
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.support.core.livedata.refresh
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentAccountInforBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.User
import com.example.nailexpress.repository.ProfileRepository
import com.google.android.libraries.places.api.model.Place
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.example.nailexpress.app.AppConfig.Status
import com.example.nailexpress.base.ActionTopBarImpl


@AndroidEntryPoint
class AccountInforFragment :
    BaseFragment<FragmentAccountInforBinding, AccountInforVM>(layoutId = R.layout.fragment_account_infor) {

    override val viewModel: AccountInforVM by viewModels()

    private val localImage get() = viewModel.profile

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()

                localImage.refresh {
                    avatar_url = (pathImage.getOrNull(0)?: "").toString()
                }
            }
        }

    override fun initView() {
        binding.apply {
            action = viewModel

            imgAvatar.onClickUploadImage = {
                FishBun.with(self)
                    .setImageAdapter(GlideAdapter())
                    .setMaxCount(1)
                    .setActionBarColor(
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        true
                    )
                    .setActionBarTitleColor(Color.parseColor("#ffffff"))
                    .startAlbumWithActivityResultCallback(imageResult)
            }

            etAddress.onClickView = {
                appSettings.openPlaceAutoComplete("", viewModel::onPlaceSelected)
            }
        }
    }
}


@HiltViewModel
class AccountInforVM @Inject constructor(
    app: Application, private val profileRepository: ProfileRepository,
) :
    BaseViewModel(app), IActionTopBar by ActionTopBarImpl() {

    val status = MutableLiveData(Status.READ)
    val profile = MutableLiveData(User())


    init {
        getProfile()
        initTopBarAction(this)
        setTitle(R.string.title_account_profile)
    }

    private fun getProfile()= launch{
        profileRepository.getProfile().onEach {
            profile.value = it
        }.collect()
    }

    fun onClickSave()= launch{
        profile.value?.apply {
            profileRepository.updateProfile(this)
            showToast(R.string.success_update_account)
            status.value = Status.READ
        }
    }

    fun onClickUpdate(){
        status.value = Status.UPDATE
    }

    fun onClickSkip(){
        status.value = Status.READ
    }

    fun onPlaceSelected(place: Place) = launch{
        val geocoder = Geocoder(getApplication())
        val listAddress = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
        profile.refresh {
            address = place.address.safe()
            latitude = place.latLng?.latitude.safe().toString()
            longitude = place.latLng?.longitude.safe().toString()
            if (listAddress!!.isNotEmpty()) {
                listAddress[0]!!.apply {
                    state = this.adminArea ?: state
                    city = this.locality ?: this.subAdminArea ?: city
                    zipcode = this.postalCode ?: zipcode
                }
            }
        }
    }
}
