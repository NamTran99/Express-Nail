package com.example.nailexpress.views.ui.main.customer.salon

import android.app.Application
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.support.core.livedata.changeValue
import android.support.core.livedata.refresh
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentCreateSalonBinding
import com.example.nailexpress.extension.convertToRequest
import com.example.nailexpress.extension.convertToResult
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.AppImage
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.views.ui.main.customer.salon.adapter.IImageLocalAdapterAction
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.google.android.libraries.places.api.model.Place
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateSalonFragment :
    BaseFragment<FragmentCreateSalonBinding, CreateSalonVM>(layoutId = R.layout.fragment_create_salon) {

    companion object {
        const val MAX_SELECTED_IMAGE = 10
    }

    override val viewModel: CreateSalonVM by viewModels()
    private val localImage get() = viewModel.form.value?.localImage ?: mutableListOf()

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()
                localImage.clear()
                localImage.addAll(
                    pathImage.convertToResult().toMutableList()
                )
                Log.d(TAG, "image size : ${localImage.size}")
                viewModel.adapter.submit(localImage)
            }
        }

    override fun initView() {
        binding.apply {
            action = viewModel

            btLoadImage.onClick {
                FishBun.with(this@CreateSalonFragment)
                    .setImageAdapter(GlideAdapter())
                    .setMaxCount(MAX_SELECTED_IMAGE)
                    .setMinCount(1)
                    .setSelectedImages(localImage.convertToRequest())
                    .setActionBarColor(
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        true
                    )
                    .setActionBarTitleColor(Color.parseColor("#ffffff"))
                    .startAlbumWithActivityResultCallback(imageResult)
            }

            etAddress.onClick {
                appSettings.openPlaceAutoComplete("", viewModel::onPlaceSelected)
            }
        }
    }
}


@HiltViewModel
class CreateSalonVM @Inject constructor(
    app: Application, private val cvRepository: CvRepository,
    private val salonRepository: SalonRepository,
) :
    BaseViewModel(app), IActionTopBar by ActionTopBarImpl(),IImageLocalAdapterAction {

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_create_salon))

    override val onRemoveImage: (item: AppImage) -> Unit = {
        form.changeValue {
            localImage.remove(it)
        }
        adapter.removeData(it)
    }

    val onItemColorSelected: ((pos: Int) -> Unit) = {
        form.changeValue {
            customer_skin_color = it - 1
        }
    }

    val adapter = ImageLocalAdapter(this)
    val form = MutableLiveData<Salon>(Salon())

    fun onPlaceSelected(place: Place) = viewModelScope.launch {
        val geocoder = Geocoder(getApplication())
        val listAddress = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
        form.refresh {
            address = place.address.safe()
            latitude = place.latLng?.latitude.safe()
            longitude = place.latLng?.longitude.safe()
            if (listAddress!!.isNotEmpty()) {
                listAddress[0]!!.apply {
                    state = this.adminArea ?: state
                    city = this.locality ?: this.subAdminArea ?: city
                    zipcode = this.postalCode ?: zipcode
                }
            }
        }
    }

    fun createSalon() = launch {
        form.value?.let{
            salonRepository.createSalon(it).collect()
        }
        showToast(R.string.success_create_salon)
        backScreen()
    }
}
