package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import android.location.Geocoder
import android.support.core.flow.stateFlowOf
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.changeValue
import android.support.core.livedata.refresh
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.base.MainApplication.Companion.application
import com.example.nailexpress.databinding.FragmentBookStaffNowBinding
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.BookServiceForm
import com.example.nailexpress.models.ui.main.BookingStaffForm
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.models.ui.main.Skill
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.dialog.picker.DatePickerDialog
import com.example.nailexpress.views.dialog.picker.TimePickerCustomDialogOwner
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.BookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IBookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.ISelectServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.SelectServiceAdapter
import com.example.nailexpress.views.ui.main.staff.dialogs.BookStaffServiceDialog
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class BookStaffFragment() :
    BaseFragment<FragmentBookStaffNowBinding, BookNowStaffVM>(layoutId = R.layout.fragment_book_staff_now),
    TimePickerCustomDialogOwner {

    override val viewModel: BookNowStaffVM by activityViewModels()
    private val selectDateDialog by lazy { DatePickerDialog(appActivity) }

    private val selectServiceDialog: BookStaffServiceDialog by lazy { BookStaffServiceDialog() }

    private val form get() = viewModel.form

    override fun initView() {
        binding.apply {
            action = viewModel

            viewModel.apply {
                form.changeValue {
                    curriculum_vitae_id = arguments?.getInt(Constant.CV_ID).safe()
                }

                viewModel.apply {
                    arguments?.getBoolean(Constant.IS_BOOK_NOW)?.let { updateTitle(it) }
                    selectedServiceDialog.bind {
                    }
                }

                showDialogSelectService.bind {
                    selectServiceDialog.show(childFragmentManager, selectServiceDialog.TAG)
                }

                etAddress.onClick {
                    appSettings.openPlaceAutoComplete("", viewModel::onPlaceSelected)
                }
            }

            selectDateDialog.apply {
                setupClickWithView(tvDate)
                onDateListener = {
                    form.changeValue {
                        date = it
                    }
                }
            }

            lvSelectDate.onClick {
                tvDate.callOnClick()
            }

            lvSelectTime.onClick {
                timePickerDialog.show() { display, api ->
                    form.changeValue {
                        time = api
                        tvTime.text = display
                    }
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
    private val salonRepository: SalonRepository,
    private val bookingStaffRepository: RecruitmentBookingStaffRepository,
    private val cvRepository: CvRepository,
    private val appEvent2: AppEvent2
) :
    BaseViewModel(application), IActionTopBar by ActionTopBarImpl(), IBookServiceAdapter,
    ISalonLayout, ISelectServiceAdapter {

    init {
        initTopBarAction(this)
    }

    override fun loadDataScreen() {
        getMySalon()
        collectSelectedService()
        getCVByID()
    }

    override val salon = MutableLiveData(Salon())
    override val imageAdapter = ImageLocalAdapter(status = AppConfig.Status.READ)

    var selectService = MutableLiveData(BookServiceForm())
    val form = MutableLiveData(BookingStaffForm())
    val isHaveSalon = MutableLiveData(false)
    val serviceAdapter = BookServiceAdapter(this)

//    val onSelectTimeType: ((Int) -> Unit) = {
//        form.changeValue {
//            bookTime.unitIndex = it
//        }
//    }

    fun updateTitle(isBookNow: Boolean) {
        form.refresh {
            this.isBookNow = isBookNow
        }
        setTitle(if (isBookNow) R.string.title_booking_staff_now else R.string.title_booking_staff_later)
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

    private fun collectSelectedService() {
        appEvent2.selectedService.drop(1).onEach {
            selectService.value = BookServiceForm(it)
        }.launchIn(viewModelScope)
    }

    fun onClickAddService() {
        form.changeValue {
            selectService.changeValue {
                this.handleAddItem(!isSelectBookingService)
                saveItem(this)
                serviceAdapter.addData(this)

                if (isSelectBookingService) {
                    selectService.value = BookServiceForm()
                } else {
                    bookTime.price = price
                    selectService.value = BookServiceForm(price = price)
                }
            }
        }
    }

    private fun getMySalon() = launch {
        salonRepository.getSalonDetail().onEach {
            isHaveSalon.value = it.isNotEmpty()
            if (it.isNotEmpty()) {
                salon.value = it[0]
                Log.d(TAG, "getMySalon: ${it[0].isImageEmpty}")
                form.changeValue {
                    salon_id = it[0].salonID
                }
                imageAdapter.submit(it[0].listImage)
            }
        }.collect()
    }

    fun onClickBySKill() {
        form.refresh {
            if (!isSelectBookingService) {
                clearListSkill()
                serviceAdapter.submit(listBookSkill)
                isSelectBookingService = true
            }
        }
    }

    fun onClickByTime() {
        form.refresh {
            if (isSelectBookingService) {
                clearListSkill()
                serviceAdapter.submit(listBookTime)
                isSelectBookingService = false
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
        form.changeValue {
            handleData()
        }?.let {
            bookingStaffRepository.bookingStaff(it).onEach { id ->
                showToast(R.string.success_booking_staff)
                navigateToDestination(
                    R.id.action_bookNowStaffFragment_to_bookingDetailFragment,
                    bundleOf(Constant.BOOKING_ID to id),
                    popUpToDes = R.id.bookNowStaffFragment,
                    inclusive = true
                )
            }.collect()
        }
    }

    override val onClickRemoveService: (BookServiceForm) -> Unit
        get() = {
            form.changeValue {
                removeItem(it)
            }
        }

    override val onVisibleRecycler: (Boolean) -> Unit
        get() = {
            form.refresh {
                isVisibleRecycler = it
            }
        }

    // for dialog
    val selectedServiceDialog = stateFlowOf(Skill())
    var textSearch = ""
    val dissmiss: SingleLiveEvent<Any> = SingleLiveEvent()
    override val onItemSelect = { service: Skill ->
        selectedServiceDialog.value = service
        dissmiss.value = Any()
    }
    val adapter: SelectServiceAdapter by lazy { SelectServiceAdapter(this) }
    val listSkill = mutableListOf<Skill>()
    val isEmptyData = MutableStateFlow(false)

    private fun getCVByID() = launch {
        form.value?.apply {
            cvRepository.getCvDetail(curriculum_vitae_id).onEach {
                listSkill.clear()
                listSkill.addAll(it.listSkill)
                adapter.submit(it.listSkill)
            }.collect()
        }
    }

    fun filterTextSearch() {
        form.changeValue {
            adapter.submit(listSkill.filter { it.name.contains(textSearch) && it.isSKill == isSelectBookingService })
            isEmptyData.value = adapter.itemCount == 0
        }
    }

    override val onLoadMoreListener: ((Int, Int) -> Unit) = { page: Int, _ ->
        // bo qua
    }

    val onSearchChange: ((text: String) -> Unit) = {
        textSearch = it
        filterTextSearch()
    }
}