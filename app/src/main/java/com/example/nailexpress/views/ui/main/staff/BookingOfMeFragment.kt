package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.app.BookingStatusDefine
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.databinding.FragmentBookingOfMeBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.helper.DriverUtils
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.utils.KEY_ID_BOOKING_DETAIL
import com.example.nailexpress.views.dialog.DialogDenied
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
    private val dialogDenied by lazy { DialogDenied() }
    override val viewModel: BookingOfMeViewModel by viewModels()

    override fun initView() {
        with(binding) {
            rvBooking.adapter = viewModel.adapter
            topBar.setOnClickOption {
                (parentFragment as? NavDashboardStaff)?.tabNotificationClick()
            }
        }

        viewModel.showDialogDenied = {
            dialogDenied.show(childFragmentManager, dialogDenied.TAG)
        }
    }
}

@HiltViewModel
class BookingOfMeViewModel @Inject constructor(
    application: Application,
    private val repository: RecruitmentBookingStaffRepository
) : BaseRefreshViewModel(application), IBookingOfMeAction {
    private var _idBookingDenied: Int? = null
    val adapter by lazy { IBookingOfMeAction.BookingOfMeAdapter(this) }
    var showDialogDenied: (() -> Unit)? = null

    init {
        getListBookingOfMe()
    }

    private fun getListBookingOfMe(page: Int = 1) {
        launch(loading = refreshLoading) {
            repository.getListBookingOfMe(page).onEach {
                adapter.submit(it, page)
            }.collect()
        }
    }

    private fun changeStatus(idBooking: Int, status: Int, message: String? = null) {
        launch(loading = refreshLoading) {
            repository.changeStatusBooking(idBooking, status, message).onEach {
                with(adapter){
                    getData().first { it.id == idBooking }.status = status
                    notifyItemChanged(getData().indexOfFirst { it.id ==idBooking })
                }
            }.collect()
        }
    }

    //không thể remove vì đang sử dụng java.getDeractorMethod để call func này
    fun changeStatusBookingDeniedAfterShowDialog(message: String = "") {
        val id = _idBookingDenied ?: return
        changeStatus(id, BookingStatusDefine.Deny.bookingStatus, message)
    }

    override fun denied(id: Int) {
        _idBookingDenied = id
        showDialogDenied?.invoke()
    }

    override fun accept(id: Int) {
        changeStatus(id, BookingStatusDefine.Accept.bookingStatus)
    }

    override fun finish(id: Int) {
        changeStatus(id, BookingStatusDefine.Finish.bookingStatus)
    }

    override fun startMoveToRendezvous(id: Int) {
        changeStatus(id, BookingStatusDefine.StartMoving.bookingStatus)
    }

    override fun iHaveArived(id: Int) {
        changeStatus(id, BookingStatusDefine.Arrived.bookingStatus)
    }

    override fun message(phone: String) {
        DriverUtils.message(getApplication(), phone)
    }

    override fun call(phone: String) {
        DriverUtils.call(getApplication(), phone)
    }


    override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
        getListBookingOfMe(page)
    }

    override fun onSwipeRefreshData() {
        getListBookingOfMe()
    }

    override fun onClickDetail(idBooking: Int) {
        navigateToDestination(
            R.id.bookingDetailStaffFragment,
            bundleOf(KEY_ID_BOOKING_DETAIL to idBooking)
        )
    }
}