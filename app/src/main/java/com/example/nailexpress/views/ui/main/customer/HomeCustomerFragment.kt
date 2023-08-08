package com.example.nailexpress.views.ui.main.customer

import android.app.Application
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentHomeCustomerBinding
import com.example.nailexpress.extension.bind
import com.example.nailexpress.extension.drawableClickRight
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.ui.main.customer.adapter.BookingCVAdapter
import com.example.nailexpress.views.ui.main.customer.adapter.IBookingCVAction
import com.example.nailexpress.views.ui.main.customer.adapter.INailStaffAction
import com.example.nailexpress.views.ui.main.customer.adapter.NailStaffAdapter
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavDashBoard
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavDashBoardDirections
import com.example.nailexpress.views.widgets.CustomHeaderHome
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeCustomerFragment :
    BaseRefreshFragment<FragmentHomeCustomerBinding, HomeCustomerVM>(layoutId = R.layout.fragment_home_customer),
    CustomHeaderHome.IActionHeader {

    override val viewModel: HomeCustomerVM by viewModels()

    override fun initView() {
        binding.apply {
            action = viewModel

            with(header) {
                updateAction(this@HomeCustomerFragment)
                updateTextNotification("3")
            }
        }
    }

    override fun onTextChange(string: String) {
    }

    override fun onClickNotification() {
        (parentFragment as? NavDashBoard)?.tabNotificationClick()
    }

    override fun onClickFilter() {
        navigateToDestination(R.id.fragmentFilterCustomer)
    }
}

@HiltViewModel
class HomeCustomerVM @Inject constructor(
    app: Application,
    private val cvRepository: CvRepository,
    private val bookingStaffRepository: RecruitmentBookingStaffRepository
) :
    BaseRefreshViewModel(app), INailStaffAction, IActionTopBar by ActionTopBarImpl(),
    IBookingCVAction, IActionTabChange {

    companion object {
        const val TAB_STAFF = 0
        const val TAB_STAFF_UNBOOKED = 1
    }

    val bookingAdapter = BookingCVAdapter(this)
    var searchText = ""
    var tabSelect = MutableLiveData(TAB_STAFF)

    init {
        title.value = getString(R.string.home_des_1)
    }

    override fun loadDataScreen() {
        loadData(1)
    }

    override val onClickBookStaff: (Int) -> Unit = { cvID ->
        navigateToDestination(
            NavDashBoardDirections.actionHomeCustomerFragmentToBookNowStaffFragment(
                cvID
            )
        )
    }
    override val onClickViewDetailBooking: (id: Int) -> Unit = {
        navigateToDestination(
            NavDashBoardDirections.actionHomeCustomerFragmentToBookingDetailFragment(
                it
            )
        )
    }

    override val onClickViewDetail: (Int) -> Unit = {
        navigateToDestination(
            R.id.action_homeCustomerFragment_to_detailStaffFragment, bundle = bundleOf(Constant.STAFF_ID to it)
        )
    }

    override val onLoadMoreListener: (Int, Int) -> Unit =
        { nextPage, _ ->
            getListStaffNail(nextPage)
        }

    val adapter = NailStaffAdapter(this).apply {
        onLoadMoreListener = { nextPage, _ ->
            loadData(nextPage)
        }
    }

    override val onSearchTextChange: (String) -> Unit
        get() = {
            searchText = it
            loadData(1)
        }

    override fun onTabChangeListener(index: Int) {
        tabSelect.value = index
        loadData(1)
    }

    private fun loadData(page: Int) {
        if (tabSelect.value == TAB_STAFF) {
            getListStaffNail(page, searchText)
        } else {
            getListCVBooking(page, searchText)
        }
    }

    override fun onSwipeRefreshData() {
        loadData(1)
    }

    private fun getListStaffNail(page: Int = 1, search: String = "") =
        launch(loading = refreshLoading) {
            cvRepository.getListCv(page = page).onEach {
                adapter.submit(it, page)
            }.collect()
        }

    private fun getListCVBooking(page: Int = 1, search: String = "") =
        launch(loading = refreshLoading) {
            bookingStaffRepository.getListBookingStaff(page = page).onEach {
                bookingAdapter.submit(it, page)
            }.collect()
        }
}

interface IActionTabChange {
    fun onTabChangeListener(index: Int)
}


