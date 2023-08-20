package com.example.nailexpress.views.ui.main.customer.booking

import android.app.Application
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentDetailStaffAppliesBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.ISalonLayout
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BookingCustomerDetailFragment :
    BaseRefreshFragment<FragmentDetailStaffAppliesBinding, BookingDetailVM>(layoutId = R.layout.fragment_detail_staff_applies) {

    override val viewModel: BookingDetailVM by viewModels()
    override fun initView() {
        binding.apply {
            viewModel.bookingID = arguments?.getInt(Constant.BOOKING_ID).safe()
            topBar.setOnBackPress { findNavController().popBackStack() }
        }

        setListener()
    }

    private fun setBookingUi(bookingDTO: BookingDTO) {
        with(binding) {
            layoutSalon.isVisible = bookingDTO.salon_id != null
            userBookedInfo.apply {
                setShowExpireTime(false)
                setID(bookingDTO.id)
                setStatusNow(bookingDTO.booking_time)
                setStatusBooking(bookingDTO.status)
                setName(bookingDTO.cv.fullname)
                setPhoneNumber(bookingDTO.cv.phone)
                setLocation(bookingDTO.address)
                setDistance(bookingDTO.distance)
            }

            jobInfoStaffView.apply {
                setCreateTime(bookingDTO.created_at)
                setOrderedTime(bookingDTO.booking_time)
                setBookingType(
                    bookingDTO.salary_type,
                    bookingDTO.work_time,
                    bookingDTO.price,
                    bookingDTO.unit
                )
            }
            serviceWorkerView.apply {
                setDataListService(bookingDTO.skills)
            }
            infoUserBookWorkerView.apply {
                customerName = bookingDTO.cv.fullname
            }
        }
    }

    private fun setUiSalon(salon: Salon?) {
        binding.layoutSalon.isVisible = salon != null
        if (salon != null) {
            with(binding) {
                shopInfoView.apply {
                    shopName = salon.name
                    phoneNumber = salon.phoneDisplay
                    location = salon.address
                }
                infoUserBookWorkerView.apply {
                    setPhoneNumberFormatPhoneUSCustom(salon.phoneDisplay)
                }

                salonActiveTime.iilValue = salon.experience_years_display
                majorityCustomerView.iilValue = salon.skinColorDisplay
                workerAccommodation.iilValue = salon.display_have_place
                shuttleBusWorker.iilValue = salon.display_have_car
                salonDescriptionView.apply {
                    description = salon.description
                }
                salonPictureView.apply {
                    setListPicture(salon.listImage)
                }
            }
        }
    }

    private fun setListener() {
        viewModel.apply {
            salon.bind {
                setUiSalon(it)
            }
            booking.bind {
                setBookingUi(it)
            }
        }

    }
}


@HiltViewModel
class BookingDetailVM @Inject constructor(
    app: Application,
    private val bookingStaffRepository: RecruitmentBookingStaffRepository,
    private val salonRepository: SalonRepository
) :
    BaseRefreshViewModel(app), IActionTopBar by ActionTopBarImpl(), ISalonLayout {

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_booking_detail))
    override val salon = MutableLiveData<Salon>()
    val cv = MutableLiveData<CvDTO>()
    val booking = MutableLiveData<BookingDTO>()
    var bookingID = 0
        set(value) {
            field = value
            getBookingByID(bookingID)
        }

    override val imageAdapter: ImageLocalAdapter
        get() = ImageLocalAdapter()


    fun getBookingByID(id: Int) = launch(loading = refreshLoading) {
        bookingStaffRepository.getDetailBookingById(id).onEach {
            it.salon_id?.let { id ->
                getSalonID(id)
            }
            cv.value = it.cv
            booking.value = it
        }.collect()
    }

    private fun getSalonID(id: Int) = launch(loading = refreshLoading) {
        salonRepository.getSalonByID(id).onEach {
            salon.value = it
            imageAdapter.submit(it.listImage)
        }.collect()
    }

    override fun onSwipeRefreshData() {
        getBookingByID(bookingID)
    }

}
