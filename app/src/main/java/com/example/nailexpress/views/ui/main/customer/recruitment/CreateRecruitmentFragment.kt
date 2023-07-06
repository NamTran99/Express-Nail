package com.example.nailexpress.views.ui.main.customer.recruitment

import android.app.Application
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.changeValue
import android.support.core.livedata.refresh
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentCreateRecruitmentBinding
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.BookServiceForm
import com.example.nailexpress.models.ui.main.RecruitmentForm
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.BookingStaffRepository
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.views.dialog.picker.DatePickerDialog
import com.example.nailexpress.views.dialog.picker.TimePickerCustomDialogOwner
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.BookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IBookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.dialogs.SelectServiceDialog
import com.google.android.libraries.places.api.model.Place
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CreateRecruitmentFragment :
    BaseFragment<FragmentCreateRecruitmentBinding, CreateRecruitmentVM>(layoutId = R.layout.fragment_create_recruitment), TimePickerCustomDialogOwner {

    private val selectDateDialog by lazy { DatePickerDialog(appActivity) }
    private val selectServiceDialog: SelectServiceDialog by lazy { SelectServiceDialog() }

    private val form get() = viewModel.recruitmentForm

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()

                form.refresh {
                    avatar = (pathImage.getOrNull(0)?: "").toString()
                    binding.imgImage.setImageUrl(avatar)
                }
            }
        }
    override val viewModel: CreateRecruitmentVM by viewModels()
    override fun initView() {
        binding.apply {
            action = viewModel
            viewModel.apply {
                showDialogSelectService.bind {
                    selectServiceDialog.show(childFragmentManager, selectServiceDialog.TAG)
                }
            }
            imgImage.onClick {
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

            etAddress.onClick {
                appSettings.openPlaceAutoComplete("", viewModel::onPlaceSelected)
            }

            selectDateDialog.apply {
                setupClickWithView(tvDate)
                onDateListener = {
                    form.changeValue {
                        date = it
                    }
                }
            }
            lvSelectDate.onClick{
                tvDate.callOnClick()
            }

            lvSelectTime.onClick{
                timePickerDialog.show(){display, api ->
                    form.changeValue {
                        time = api
                        tvTime.text = display
                    }
                }
            }
        }
    }
}

interface ICreateSalonVM {
    val salonForm: MutableLiveData<Salon>
    val salonImageAdapter: ImageLocalAdapter
    val onItemColorSelected: ((pos: Int) -> Unit)
}

interface ICreateRecruitmentVM {
    val onItemGenderSelected: ((pos: Int) -> Unit)
}

@HiltViewModel
class CreateRecruitmentVM @Inject constructor(
    val app: Application,
    private val cvRepository: CvRepository,
    private val bookingStaffRepository: BookingStaffRepository,
    private val salonRepository: SalonRepository,
    val appEvent2: AppEvent2
) :
    BaseViewModel(app), IActionTopBar, ICreateSalonVM, ICreateRecruitmentVM, IBookServiceAdapter {

    override val title = MutableLiveData(getString(R.string.title_create_recruitment))
    override val salonForm = MutableLiveData(Salon())
    override val salonImageAdapter = ImageLocalAdapter()
    val serviceAdapter = BookServiceAdapter(this)
    var selectService = MutableLiveData(BookServiceForm())

    val onSelectTimeType: ((Int) -> Unit) = {
        selectService.changeValue {
            unitIndex = it
        }
    }

    override val onItemColorSelected: (pos: Int) -> Unit = {
        salonForm.changeValue {
            customer_skin_color = it - 1
        }
    }

    override val onItemGenderSelected: (pos: Int) -> Unit = {
        salonForm.changeValue {
            customer_skin_color = it - 1
        }
    }

    init {
        collectSelectedService()
    }

    val recruitmentForm = MutableLiveData(RecruitmentForm())
    val isShowSalon = MutableLiveData(false)
    var isLoadSalonData = false

    private fun collectSelectedService() {
        appEvent2.selectedService.drop(1).onEach {
            selectService.value = BookServiceForm(it)
        }.launchIn(viewModelScope)
    }

    fun onClickBySKill(){
        recruitmentForm.refresh {
            if (!isBookingBySkill) {
                clearListSkill()
                serviceAdapter.clear()
                isBookingBySkill = true
            }
        }
    }

    fun onClickByTime() {
        recruitmentForm.refresh {
            if (isBookingBySkill) {
                clearListSkill()
                serviceAdapter.clear()
                isBookingBySkill = false
            }
        }
    }

    fun onClickAddService() {
        recruitmentForm.changeValue {
            selectService.refresh {
                this.handleToDisplayUI(app)
                listCustom.add(this)
                serviceAdapter.addData(this)
            }
        }
        selectService.value = BookServiceForm()
    }

    val showDialogSelectService = SingleLiveEvent<Any>()

    fun onClickSelectService() {
        showDialogSelectService.refresh()
    }

    fun onPlaceSelected(place: Place) = launch{
        val geocoder = Geocoder(getApplication())
        val listAddress = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
        recruitmentForm.refresh {
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

    fun createBooking() = launch {
        salonForm.value?.apply {
            salonRepository.createSalon(this).onEach {
            }.collect()
        }
    }

    fun onClickShowSalon() {
        isShowSalon.value = true
        if (!isLoadSalonData) {
            getMySalon()
        }
    }

    fun onClickHideSalon() {
        isShowSalon.value = false
    }



    private fun getMySalon() = launch {
        salonRepository.getSalonDetail().onEach {
            isLoadSalonData = true
            if (it.isNotEmpty()) {
                salonForm.value = it[0]
                recruitmentForm.changeValue {
                    salon_id = it[0].salonID
                }
                salonImageAdapter.submit(it[0].listImage)
            }
        }.collect()
    }

    override val onClickRemoveService: (BookServiceForm) -> Unit= {
        recruitmentForm.changeValue {
            listCustom.remove(it)
        }
    }
    override val onVisibleItem: (Boolean) -> Unit= {
        recruitmentForm.refresh {
            isVisibleRecycler = it
        }
    }
}
