package com.example.nailexpress.views.ui.main.staff.booking_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentBookingDetailStaffBinding
import com.example.nailexpress.views.ui.main.staff.booking_detail.viewmodel.BookingDetailStaffViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingDetailStaffFragment : BaseFragment<FragmentBookingDetailStaffBinding, BookingDetailStaffViewModel>(
    R.layout.fragment_detail_post_staff
) {
    override val viewModel: BookingDetailStaffViewModel by viewModels()

    override fun initView() {
    }

    private fun setUi() {
        with(binding) {
            userBookedInfo.apply {  }
        }
    }
}