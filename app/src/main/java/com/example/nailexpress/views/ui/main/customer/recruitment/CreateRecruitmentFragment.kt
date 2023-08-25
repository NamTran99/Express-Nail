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
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentCreateRecruitmentBinding
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.extension.convertToRequest
import com.example.nailexpress.extension.convertToResult
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.BookServiceForm
import com.example.nailexpress.models.ui.main.RecruitmentForm
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.dialog.picker.DatePickerDialog
import com.example.nailexpress.views.dialog.picker.TimePickerCustomDialogOwner
import com.example.nailexpress.views.ui.main.customer.salon.CreateSalonFragment
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.BookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IBookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.cv_profile.CreateCvVM
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
    BaseFragment<FragmentCreateRecruitmentBinding, CreateRecruitmentVM>(layoutId = R.layout.fragment_create_recruitment),
    TimePickerCustomDialogOwner {

    private val selectDateDialog by lazy { DatePickerDialog(appActivity) }
    private val selectServiceDialog: SelectServiceDialog by lazy { SelectServiceDialog() }

    private val form get() = viewModel.recruitmentForm
    private val localImage get() = viewModel.salonForm.value?.localImage ?: mutableListOf()

    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()

                form.refresh {
                    avatar = (pathImage.getOrNull(0) ?: "").toString()
                    binding.imgImage.setImageUrl(avatar)
                }
            }
        }

    private val moreImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()
                localImage.clear()
                localImage.addAll(
                    pathImage.convertToResult().toMutableList()
                )
                viewModel.salonImageAdapter.submit(localImage)
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

            salonView.btLoadImage.onClick{
                FishBun.with(this@CreateRecruitmentFragment)
                    .setImageAdapter(GlideAdapter())
                    .setMaxCount(CreateSalonFragment.MAX_SELECTED_IMAGE)
                    .setMinCount(1)
                    .setSelectedImages(localImage.convertToRequest())
                    .setActionBarColor(
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        true
                    )
                    .setActionBarTitleColor(Color.parseColor("#ffffff"))
                    .startAlbumWithActivityResultCallback(moreImageResult)
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
                appSettings.openPlaceAutoComplete("", viewModel::onRecruitmentPlaceSelected)
            }

            salonView.etSalonAddress.onClick{
                appSettings.openPlaceAutoComplete("", viewModel::onSalonPlaceSelected)
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
    private val recruitmentRepo: RecruitmentBookingStaffRepository,
    private val salonRepository: SalonRepository,
    val appEvent2: AppEvent2
) :
    BaseViewModel(app), IActionTopBar by ActionTopBarImpl(), ICreateSalonVM, ICreateRecruitmentVM,
    IBookServiceAdapter {

    override val title = MutableLiveData(getString(R.string.title_create_recruitment))
    val recruitmentForm = MutableLiveData(RecruitmentForm())
    override val salonForm = MutableLiveData(Salon())
    override val salonImageAdapter = ImageLocalAdapter()

    var inputSKillByService = MutableLiveData(BookServiceForm())
    var inputSKillByTime = MutableLiveData(BookServiceForm())

    val adapterSkillByTime: BookServiceAdapter by lazy { BookServiceAdapter(this) }
    val adapterSkillByService: BookServiceAdapter by lazy {
        BookServiceAdapter(
            callbackSkillByService
        )
    }

    var selectServiceType = CreateCvVM.SelectServiceType

    val callbackSkillByService = object : IBookServiceAdapter {
        override val onClickRemoveService: (BookServiceForm) -> Unit = {
            recruitmentForm.changeValue {
                listBookSkill.remove(it)
            }
        }
        override val onVisibleRecycler: (Boolean) -> Unit = {

        }
    }

    val isShowSalon = MutableLiveData(false)
    private var isLoadSalonData = false

    init {
        collectSelectedService()
        initTopBarAction(this)
    }

    val onSelectTimeType: ((Int) -> Unit) = {
        recruitmentForm.changeValue {
            salaryTimeValue.unit = it.toString()
        }
    }

    val onEnableSkillBySkill: ((Boolean) -> Unit) = { it ->
        recruitmentForm.refresh {
            isSelectBookingService = it
        }
    }

    val onEnableSkillByTime: ((Boolean) -> Unit) = { it ->
        recruitmentForm.refresh {
            isSelectBookingTime = it
        }
    }


    override val onItemColorSelected: (pos: Int) -> Unit = {
        salonForm.changeValue {
            customer_skin_color = it - 1
        }
    }

    override val onItemGenderSelected: (pos: Int) -> Unit = {
        recruitmentForm.changeValue {
            gender = it - 1
        }
    }


    private fun collectSelectedService() {
        appEvent2.selectedService.drop(1).onEach {
            if (selectServiceType == CreateCvVM.SelectServiceType) {
                inputSKillByService.value = BookServiceForm(it)
            } else {
                inputSKillByTime.value = BookServiceForm(it)
            }
        }.launchIn(viewModelScope)
    }

    fun onClickAddSkillByService() {
        recruitmentForm.refresh {
            inputSKillByService.refresh {
                saveItem(handleAddItem(false))
                adapterSkillByService.addData(this)
            }
        }
        inputSKillByService.value = BookServiceForm()
    }

    fun onClickAddSkillByTime() {
        recruitmentForm.refresh {
            inputSKillByTime.refresh {
                saveItem(handleAddItem(true))
                adapterSkillByTime.addData(this)
//                salaryTimeValue.price = price
            }
            inputSKillByTime.value = BookServiceForm()
        }
    }



    val showDialogSelectService = SingleLiveEvent<Any>()

    fun onClickSelectService(content: String, serviceType: Int) {
        selectServiceType = serviceType
        showDialogSelectService.value = content
    }

    fun onRecruitmentPlaceSelected(place: Place) = launch {
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

    fun onSalonPlaceSelected(place: Place) = launch {
        val geocoder = Geocoder(getApplication())
        val listAddress = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
        salonForm.refresh {
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

    fun onClickShowSalon() {
        isShowSalon.value = true
        if (!isLoadSalonData) {
            getMySalon()
        }
    }

    fun onClickHideSalon() {
        isShowSalon.value = false
    }

    fun onClickSubmit() = launch {
        recruitmentForm.value?.let {
            salonForm.value?.let { salon ->
                isShowSalon.value?.let { isShow ->
                    recruitmentRepo.createRecruitment(it, salon, isShow)?.let{
                        showToast(R.string.success_create_recruitment)
                        navigateToDestination(R.id.action_createRecruitmentFragment_to_detailPostCustomerFragment, bundle = bundleOf(
                            Constant.RECRUIMENT_ID to it), popUpToDes = R.id.navDashBoard)
                    }
                }
            }
        }
    }

    fun getBgBtnSelectBooking(isSelect: Boolean, isBooking: Boolean) = ContextCompat.getDrawable(
        getApplication(), when {
            isSelect && !isBooking -> R.drawable.bg_btn_stroke_state_1
            isSelect && isBooking -> R.drawable.bg_btn_stroke_state_2
            !isSelect && isBooking -> R.drawable.bg_btn_stroke_state_4
            else -> R.drawable.bg_btn_stroke_state_3
        }
    )

    fun getTextBtnSelectBooking(isSelect: Boolean, isBooking: Boolean) = ContextCompat.getColor(
        getApplication(), when {
            isSelect && !isBooking -> R.color.white
            isSelect && isBooking -> R.color.black
            !isSelect && isBooking -> R.color.black
            else -> R.color.white
        }
    )

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

    override val onClickRemoveService: (BookServiceForm) -> Unit = {
        recruitmentForm.refresh {
            removeItem(it)
        }
    }
    override val onVisibleRecycler: (Boolean) -> Unit = {
        recruitmentForm.refresh {
            isVisibleRecycler = it
        }
    }
}
