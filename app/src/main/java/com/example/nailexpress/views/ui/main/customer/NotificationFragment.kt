package com.example.nailexpress.views.ui.main.customer

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.repository.NotificationRepository
import com.example.nailexpress.views.ui.main.staff.NotificationStaff
import com.example.nailexpress.views.ui.main.staff.NotificationStaffViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : NotificationStaff(){
    override val viewModel: NotificationStaffViewModel by viewModels<NotificationVM>()
}

@HiltViewModel
class NotificationVM @Inject constructor(
    application: Application,
    notifyRepository: NotificationRepository
) : NotificationStaffViewModel(
    application, notifyRepository
){
    override fun onClickDetail() {
        navigateToDestination(R.id.detailStaffFragment)
    }
}

