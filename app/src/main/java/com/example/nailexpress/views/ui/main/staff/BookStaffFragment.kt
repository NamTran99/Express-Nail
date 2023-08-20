package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import android.location.Geocoder
import android.support.core.flow.stateFlowOf
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.changeValue
import android.support.core.livedata.refresh
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.app.SalaryType
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
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.models.ui.main.Skill
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.dialog.picker.DatePickerDialog
import com.example.nailexpress.views.dialog.picker.TimePickerCustomDialogOwner
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.BookSelectServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.ISelectServiceAdapter
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BookStaffFragment() :
    BaseFragment<FragmentBookStaffNowBinding, BookNowStaffVM>(layoutId = R.layout.fragment_book_staff_now),
    TimePickerCustomDialogOwner {

    override val viewModel: BookNowStaffVM by viewModels()
    private val selectDateDialog by lazy { DatePickerDialog(appActivity) }

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
    BaseViewModel(application), IActionTopBar by ActionTopBarImpl(),
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
    val staffCV = MutableLiveData<Cv>()
    val isHaveSalon = MutableLiveData(false)
    val serviceAdapter : BookSelectServiceAdapter by lazy {  BookSelectServiceAdapter() }

    private var listSkillService = listOf<Skill>()
    private var listSkillTime = listOf<Skill>()

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



    fun onClickBySKill() {
        form.refresh {
            isSelectBookingService = true
        }
        serviceAdapter.submit(listSkillService)
    }

    fun onClickByTime() {
        form.refresh {
            isSelectBookingService = false
        }
        serviceAdapter.submit(listSkillTime)
    }

    fun onClickCreateSalon() {
        navigateToDestination(R.id.action_bookNowStaffFragment_to_createSalonFragment)
    }

    fun onClickSubmit() = launch {
        form.changeValue {
            adapterSelectListItem = serviceAdapter.mitems
        }

        form.value?.let {
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

    // for dialog
    val selectedServiceDialog = stateFlowOf(Skill())
    val dissmiss: SingleLiveEvent<Any> = SingleLiveEvent()
    override val onItemSelect = { service: Skill ->
        selectedServiceDialog.value = service
        dissmiss.value = Any()
    }

    private fun setUpServicesFirstTimeLoading(cv: Cv){
        when(cv.salaryType){
            SalaryType.Service.data, SalaryType.Both.data -> {
                onClickBySKill()
            }
            SalaryType.Time.data -> {
                onClickByTime()
            }
        }
    }

    private fun collectSelectedService() {
        appEvent2.selectedService.drop(1).onEach {
            selectService.value = BookServiceForm(it)
        }.launchIn(viewModelScope)
    }

    private fun getCVByID() = launch {
        form.value?.apply {
            cvRepository.getCvDetail(curriculum_vitae_id).onEach {
                listSkillService = it.listSkill.filter {
                    it.isService
                }
                listSkillTime = it.listSkill.filter {
                    !it.isService
                }
                staffCV.value = it
                setUpServicesFirstTimeLoading(it)
            }.collect()
        }
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

    override val onLoadMoreListener: ((Int, Int) -> Unit) = { page: Int, _ ->
        // bo qua
    }


}