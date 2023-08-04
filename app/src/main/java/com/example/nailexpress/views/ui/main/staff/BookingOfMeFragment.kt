package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentBookingOfMeBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.views.ui.main.staff.adapter.BookingOfMeAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IBookingOfMeAction
import com.example.nailexpress.views.ui.main.staff.nav_staff.NavDashboardStaff
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BookingOfMeFragment :
    BaseRefreshFragment<FragmentBookingOfMeBinding, BookingOfMeViewModel>(R.layout.fragment_booking_of_me) {
    override val viewModel: BookingOfMeViewModel by viewModels()

    override fun initView() {
        with(binding) {
            rvBooking.adapter = viewModel.adapter
            topBar.setOnClickOption {
                (parentFragment as? NavDashboardStaff)?.tabNotificationClick()
            }
        }
    }
}

@HiltViewModel
class BookingOfMeViewModel @Inject constructor(
    application: Application, private val repository: RecruitmentBookingStaffRepository
) : BaseRefreshViewModel(application), IBookingOfMeAction {
    val adapter by lazy { BookingOfMeAdapter(this) }

    init {
        getListBookingOfMe()
    }

    private fun getListBookingOfMe(page: Int = 1) {
        launch(loading = refreshLoading) {
            repository.getListBookingOfMe(page).onEach {
                adapter.submit(it,page)
            }.collect()
        }
    }

    override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
        getListBookingOfMe(page)
    }

    override fun onSwipeRefreshData() {
        getListBookingOfMe()
    }
}