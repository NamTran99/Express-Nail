package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.databinding.FragmentNotificationStaffBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.NotificationRepository
import com.example.nailexpress.views.ui.main.staff.adapter.AdapterNotification
import com.example.nailexpress.views.ui.main.staff.adapter.IActionNotification
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
open class NotificationStaff :
    BaseRefreshFragment<FragmentNotificationStaffBinding, NotificationStaffViewModel>(
        R.layout.fragment_notification_staff
    ) {
    override val viewModel: NotificationStaffViewModel by viewModels()
    override fun initView() {
        with(binding) {
            vm = viewModel
        }
    }
}

@HiltViewModel
open class NotificationStaffViewModel @Inject constructor(
    application: Application,
    private val notifyRepository: NotificationRepository
) : BaseRefreshViewModel(application), IActionNotification {
    val adapter by lazy { AdapterNotification(this) }
    override fun onSwipeRefreshData() {
        loadDataMyNotification()
    }

    init {
        loadDataMyNotification()
    }

    private fun loadDataMyNotification(page: Int = 1) {
        launch(loading = refreshLoading) {
            notifyRepository.getListMyNotificationStaff(page).onEach {
                adapter.submit(it, page)
            }.collect()
        }
    }

    override fun onClickDetail() {

    }

    override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
        loadDataMyNotification(page)
    }
}