package com.example.nailexpress.views.ui.main.customer.booking

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.base.*
import com.example.nailexpress.databinding.FragmentBookingDetailBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.ui.main.Booking
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.repository.BookingStaffRepository
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
class BookingDetailFragment :
    BaseRefreshFragment<FragmentBookingDetailBinding, BookingDetailVM>(layoutId = R.layout.fragment_booking_detail) {

    override val viewModel: BookingDetailVM by viewModels()
    val args: BookingDetailFragmentArgs by navArgs()
    override fun initView() {
        binding.apply {
            action = viewModel
        }
    }

    override fun onResume() {
        viewModel.getBookingByID(args.bookingID)
        super.onResume()
    }

    override fun onRefreshListener() {

    }

    override fun registerRefreshLoading() {
    }


}


@HiltViewModel
class BookingDetailVM @Inject constructor(
    app: Application,
    private val bookingStaffRepository: BookingStaffRepository,
    private val salonRepository: SalonRepository
) :
    BaseViewModel(app), IVMRefreshStatus, IActionTopBar, ISalonLayout {

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_booking_detail))
    override val salon = MutableLiveData<Salon>()
    val cv = MutableLiveData<Cv>()
    val booking = MutableLiveData<Booking>()

    override val imageAdapter: ImageLocalAdapter
        get() = ImageLocalAdapter()

    val serviceAdapter = DetailServiceAdapter()

    fun getBookingByID(id: Int)= launch{
        bookingStaffRepository.getBookingById(id).onEach {
            getSalonID(it.salon_id)
            serviceAdapter.submit(it.listSkill)
            cv.value = it.cv
            booking.value = it
        }.collect()
    }

    private fun getSalonID(id: Int) = launch {
        salonRepository.getSalonByID(id).onEach {
            salon.value = it
            imageAdapter.submit(it.listImage)
        }.collect()
    }

}
