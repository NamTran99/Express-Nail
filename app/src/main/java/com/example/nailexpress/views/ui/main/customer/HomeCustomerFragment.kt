package com.example.nailexpress.views.ui.main.customer

import android.app.Application
import android.support.core.event.LoadingEvent
import android.util.Log
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide.init
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.base.IVMRefreshStatus
import com.example.nailexpress.databinding.FragmentHomeCustomerBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.toJson
import com.example.nailexpress.repository.BookingStaffRepository
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.utils.TabChangeCallBack
import com.example.nailexpress.utils.ViewModelHandleUtils
import com.example.nailexpress.views.ui.main.customer.HomeCustomerVM.Companion.TAB_STAFF
import com.example.nailexpress.views.ui.main.customer.adapter.BookingCVAdapter
import com.example.nailexpress.views.ui.main.customer.adapter.IBookingCVAction
import com.example.nailexpress.views.ui.main.customer.adapter.INailStaffAction
import com.example.nailexpress.views.ui.main.customer.adapter.NailStaffAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeCustomerFragment :
    BaseFragment<FragmentHomeCustomerBinding, HomeCustomerVM>(layoutId = R.layout.fragment_home_customer) {


    override val viewModel: HomeCustomerVM by viewModels()
    override fun initView() {
        binding.apply {
            action = viewModel
            val tab1 = tabLayout.newTab().apply {
                setText(R.string.home_customer_tab_1)
            }
            val tab2 = tabLayout.newTab().apply {
                setText(R.string.home_customer_tab_2)
            }
            tabLayout.addTab(tab1)
            tabLayout.addTab(tab2)
        }
    }
}

@HiltViewModel
class HomeCustomerVM @Inject constructor(
    app: Application, private val cvRepository: CvRepository, private val
    bookingStaffRepository: BookingStaffRepository
) :
    BaseViewModel(app), IVMRefreshStatus, INailStaffAction, IActionTopBar, IBookingCVAction {

    companion object {
        const val TAB_STAFF = 0
        const val TAB_STAFF_UNBOOKED = 1
    }


    val adapter = NailStaffAdapter(this).apply {
        onLoadMoreListener = { nextPage, _ ->
            loadData(nextPage)
        }
    }

    val bookingAdapter = BookingCVAdapter(this)

    var searchText = ""
    var tabSelect = MutableLiveData(TAB_STAFF)

    init {
        title.value = getString(R.string.home_des_1)
    }

    override val onClickBookStaff: (Int) -> Unit = { cvID ->
        navigateToDestination(
            HomeCustomerFragmentDirections.actionHomeCustomerFragmentToBookNowStaffFragment(
                cvID
            )
        )
    }
    override val onClickViewDetailBooking: (id: Int) -> Unit=  {
        navigateToDestination(
            HomeCustomerFragmentDirections.actionHomeCustomerFragmentToBookingDetailFragment(
                it
            )
        )
    }

    override val onClickViewDetail: (Int) -> Unit = {
        navigateToDestination(
            HomeCustomerFragmentDirections.actionHomeCustomerFragmentToDetailStaffFragment(
                it
            )
        )
    }

    override val onLoadMoreListener: (Int, Int) -> Unit =
        { nextPage, _ ->
            getListStaffNail(nextPage)
        }

    override val onSearchTextChange: (String) -> Unit
        get() = {
            searchText = it
            loadData(1)
        }

    val onTabChangeListener: TabChangeCallBack
        get() = {
            tabSelect.value = it
            loadData(1)
        }

    fun loadData(page: Int) {
        if (tabSelect.value == TAB_STAFF) {
            getListStaffNail(page, searchText)
        } else {
            getListCVBooking(page, searchText)
        }
    }

    //  Load new
    override fun reloadData() {
        loadData(1)
    }

    override val refreshLoading: LoadingEvent
        get() = ViewModelHandleUtils.isLoading


    private fun getListStaffNail(page: Int = 1, search: String = "") = launch {
        cvRepository.getListCv(page = page).onEach {
            if (page == 1) {
                adapter.clear()
            }
            adapter.submit(it)
        }.collect()
    }

    private fun getListCVBooking(page: Int = 1, search: String = "") = launch {
        bookingStaffRepository.getListBookingStaff(page = page).onEach {
            if (page == 1) {
                bookingAdapter.clear()
            }
            bookingAdapter.submit(it)
        }.collect()
    }
}
