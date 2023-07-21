package com.example.nailexpress.views.ui.main.customer

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.*
import com.example.nailexpress.databinding.FragmentNotificationBinding
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
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
    private val bookingStaffRepository: RecruitmentBookingStaffRepository
) :
    BaseViewModel(app), IVMRefreshStatus, IActionTopBar by ActionTopBarImpl() {


}
