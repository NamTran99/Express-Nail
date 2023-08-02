package com.example.nailexpress.views.ui.main.staff.booking_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentBookingDetailStaffBinding
import com.example.nailexpress.models.response.BookingDTO
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.utils.KEY_ID_BOOKING_DETAIL
import com.example.nailexpress.views.ui.main.staff.booking_detail.viewmodel.BookingDetailStaffViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingDetailStaffFragment : BaseFragment<FragmentBookingDetailStaffBinding, BookingDetailStaffViewModel>(
    R.layout.fragment_booking_detail_staff
) {
    override val viewModel: BookingDetailStaffViewModel by viewModels()
    private val id: Int? by lazy {
        arguments?.getInt(KEY_ID_BOOKING_DETAIL)
    }

    override fun initView() {
        with(binding) {
            layoutContent.isVisible = id != null
            tvAccept.setOnClickListener { }
            tvDeny.setOnClickListener { }
            topBar.setOnBackPress { findNavController().popBackStack() }
        }
        id?.let {
            viewModel.getBookingById(it)
        }
        viewModel.dataBookingDetail.observe(viewLifecycleOwner) {
            setUi(it)
        }
        viewModel.salonData.observe(viewLifecycleOwner) {
            setUiSalon(it)
        }
    }

    private fun setUi(bookingDTO: BookingDTO?) {
        binding.layoutContent.isVisible = bookingDTO != null
        if (bookingDTO != null) {
            bookingDTO.salon_id?.let {
                viewModel.getSalonById(it)
            }
            with(binding) {
                layoutSalon.isVisible = bookingDTO.salon_id != null
                userBookedInfo.apply {
                    setID(bookingDTO.id)
                    setShowExpireTime(bookingDTO.booking_time.isNullOrBlank())
                    setStatusNow(bookingDTO.booking_time)
                    setStatusBooking(bookingDTO.status)
                    setName(bookingDTO.contact_name)
                    setPhoneNumber(bookingDTO.contact_phone)
                    setLocation(bookingDTO.address)
                    setDistance(bookingDTO.distance)
                }

                jobInfoStaffView.apply {
                    setCreateTime(bookingDTO.created_at)
                    setOrderedTime(bookingDTO.booking_time)
                    setBookingType(bookingDTO.salary_type)
                }
                serviceWorkerView.apply {
                    setDataListService(bookingDTO.skills)
                }
                infoUserBookWorkerView.apply {
                    customerName = bookingDTO.contact_name
                    setPhoneNumberFormatPhoneUSCustom(bookingDTO.contact_phone)
                }

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
}