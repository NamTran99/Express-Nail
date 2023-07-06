package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import android.location.Geocoder
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.changeValue
import android.support.core.livedata.refresh
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentBookStaffNowBinding
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.BookServiceForm
import com.example.nailexpress.models.ui.main.BookingStaffForm
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.BookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.BookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IBookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.dialogs.SelectServiceDialog
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BookNowStaffFragment() :
    BaseFragment<FragmentBookStaffNowBinding, BookNowStaffVM>(layoutId = R.layout.fragment_book_staff_now) {

    private val args: BookNowStaffFragmentArgs by navArgs()
    override val viewModel: BookNowStaffVM by viewModels()

    private val selectServiceDialog: SelectServiceDialog by lazy { SelectServiceDialog() }

    override fun initView() {
        binding.apply {
            action = viewModel

            viewModel.apply {
                form.changeValue {
                    curriculum_vitae_id = args.cvID
                }

                showDialogSelectService.bind {
                    selectServiceDialog.show(childFragmentManager, selectServiceDialog.TAG)
                }

                etAddress.onClick {
                    appSettings.openPlaceAutoComplete("", viewModel::onPlaceSelected)
                }
            }
        }
    }
}

interface ISalonLayout {
    val salon: MutableLiveData<Salon>
    val imageAdapter: ImageLocalAdapter
}

@HiltViewModel
class BookNowStaffVM @Inject constructor(
    val app: Application,
    val salonRepository: SalonRepository,
    val bookingStaffRepository: BookingStaffRepository,
    val appEvent2: AppEvent2
) :
    BaseViewModel(app), IActionTopBar, IBookServiceAdapter, ISalonLayout {

    override fun reloadData() {
        getMySalon()
        collectSelectedService()
    }

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_detail_staff))

    override val salon = MutableLiveData(Salon())
    val isHaveSalon = MutableLiveData(false)
    val form = MutableLiveData(BookingStaffForm())
    val serviceAdapter = BookServiceAdapter(this)
    override val imageAdapter = ImageLocalAdapter(status = AppConfig.Status.READ)

    var selectService = MutableLiveData(BookServiceForm())

    val onSelectTimeType: ((Int) -> Unit) = {
        selectService.changeValue {
            unitIndex = it
        }
    }

    fun onPlaceSelected(place: Place) {
        val geocoder = Geocoder(getApplication())
        val listAddress = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
        form.refresh {
            address = place.address.safe()
            latitude = place.latLng?.latitude.safe().toString()
            longitude = place.latLng?.longitude.safe().toString()
            if (listAddress!!.isNotEmpty()) {
                listAddress[0]!!.apply {
                    state = this.adminArea ?: state
                    city = this.locality ?: this.subAdminArea ?: city
                    zipcode = this.postalCode?.toIntOrNull() ?: zipcode
                }
            }
        }
    }

    fun collectSelectedService() {
        appEvent2.selectedService.drop(1).onEach {
            selectService.value = BookServiceForm(it)
        }.launchIn(viewModelScope)
    }

    fun onClickAddService() {
        form.changeValue {
            selectService.refresh {
                this.handleToDisplayUI(app)
                listCustom.add(this)
                serviceAdapter.addData(this)
            }
        }
        selectService.value = BookServiceForm()
    }

    private fun getMySalon() = launch {
        salonRepository.getSalonDetail().onEach {
            isHaveSalon.value = it.isNotEmpty()
            if (it.isNotEmpty()) {
                salon.value = it[0]
                form.changeValue {
                    salon_id = it[0].salonID
                }
                imageAdapter.submit(it[0].listImage)
            }
        }.collect()
    }

    fun onClickBySKill() {
        form.refresh {
            if (!isBookingBySkill) {
                clearListSkill()
                serviceAdapter.clear()
                isBookingBySkill = true
            }
        }
    }

    fun onClickByTime() {
        form.refresh {
            if (isBookingBySkill) {
                clearListSkill()
                serviceAdapter.clear()
                isBookingBySkill = false
            }
        }
    }

    fun onClickCreateSalon() {
        navigateToDestination(R.id.action_bookNowStaffFragment_to_createSalonFragment)
    }

    val showDialogSelectService = SingleLiveEvent<Any>()

    fun onClickSelectService() {
        showDialogSelectService.refresh()
    }

    fun onClickSubmit() = launch {
        form.change Value {
            handleData()
        }?.let {
            bookingStaffRepository.bookingStaff(it)
        }
    }

    override val onClickRemoveService: (BookServiceForm) -> Unit
        get() = {
            form.changeValue {
                listCustom.remove(it)
            }
        }
    override val onVisibleItem: (Boolean) -> Unit
        get() = {
            form.refresh {
                isVisibleRecycler = it
            }
        }
}