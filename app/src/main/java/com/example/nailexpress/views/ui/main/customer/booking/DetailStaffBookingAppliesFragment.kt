package com.example.nailexpress.views.ui.main.customer.booking

import android.app.Application
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.base.*
import com.example.nailexpress.databinding.FragmentDetailStaffAppliesBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.ui.main.Booking
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.repository.SalonRepository
import com.example.nailexpress.views.ui.main.customer.salon.adapter.ImageLocalAdapter
import com.example.nailexpress.views.ui.main.staff.ISalonLayout
import com.example.nailexpress.views.ui.main.staff.adapter.DetailServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BookingCustomerDetailFragment :
    BaseRefreshFragment<FragmentDetailStaffAppliesBinding, BookingDetailVM>(layoutId = R.layout.fragment_detail_staff_applies) {

    override val viewModel: BookingDetailVM by viewModels()
    val args: BookingDetailFragmentArgs by navArgs()
    override fun initView() {
        binding.apply {
            action = viewModel
            viewModel.bookingID= args.bookingID
        }

        setListener()
    }
    private fun setListener(){
        viewModel.salon.bind{
            setSalonUi(it)
        }
    }

    private fun setSalonUi(salon: Salon?) {
        with(binding) {
            if (salon != null) {
                shopInfoView.apply {
                    isVisible = salon.name.isNotBlank() && salon.phoneDisplay.isNotBlank() && salon.address.isNotBlank()
                    shopName = salon.name
                    phoneNumber = salon.phoneDisplay
                    location = salon.address
                }

                itemInfoSalonActive.apply {
                    iilValue = salon.experience_years_display
                }

                majorityCustomerView.apply {
                    iilValue = salon.skinColorDisplay
                }

                workerAccommodation.apply {
                    iilValue = salon.display_have_place
                }

                shuttleBusWorker.apply {
                    iilValue = salon.display_have_car
                }

                salonDescriptionView.apply {
                    description = salon.description
                }

                salonPictureView.apply {
                    setListPicture(salon.listImage)
                }
            } else {
                shopInfoView.isVisible = false
                itemInfoSalonActive.isVisible = false
                majorityCustomerView.isVisible = false
                workerAccommodation.isVisible = false
                shuttleBusWorker.isVisible = false
                salonDescriptionView.isVisible = false
                salonPictureView.isVisible = false
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
    BaseRefreshViewModel(app), IActionTopBar by ActionTopBarImpl(),ISalonLayout {

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_booking_detail))
    override val salon = MutableLiveData<Salon>()
    val cv = MutableLiveData<Cv>()
    val booking = MutableLiveData<Booking>()
    var bookingID = 0

    override val imageAdapter: ImageLocalAdapter
        get() = ImageLocalAdapter()

    val serviceAdapter = DetailServiceAdapter()

    private fun getBookingByID(id: Int)= launch(loading = refreshLoading){
        bookingStaffRepository.getBookingById(id).onEach {
            getSalonID(it.salon_id)
            serviceAdapter.submit(it.listSkill)
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
