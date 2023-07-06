package com.example.nailexpress.views.ui.main.customer

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.base.IVMRefreshStatus
import com.example.nailexpress.databinding.FragmentHomeCustomerBinding
import com.example.nailexpress.databinding.FragmentNotificationBinding
import com.example.nailexpress.repository.BookingStaffRepository
import com.example.nailexpress.repository.CvRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationVM>(layoutId = R.layout.fragment_notification) {

    override val viewModel: NotificationVM by viewModels()
    override fun initView() {
        binding.apply {
            action = viewModel

        }

    }
}


@HiltViewModel
class NotificationVM @Inject constructor(
    app: Application,
    private val cvRepository: CvRepository,
    private val bookingStaffRepository: BookingStaffRepository
) :
    BaseViewModel(app), IVMRefreshStatus, IActionTopBar {


}
