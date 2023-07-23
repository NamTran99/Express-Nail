package com.example.nailexpress.views.ui.main.customer

import android.app.Application
import android.util.Log
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentMyPostBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.utils.ViewModelHandleUtils
import com.example.nailexpress.views.ui.main.customer.adapter.IMyPostAction
import com.example.nailexpress.views.ui.main.customer.adapter.MyPostAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MyPostFragment :
    BaseRefreshFragment<FragmentMyPostBinding, MyPostViewModel>(R.layout.fragment_my_post) {
    override val viewModel: MyPostViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
    }
}

@HiltViewModel
class MyPostViewModel @Inject constructor(
    application: Application,
    private val recruitmentRepository: RecruitmentBookingStaffRepository
) : BaseRefreshViewModel(application), IMyPostAction {
    val adapter by lazy { MyPostAdapter(this) }

    init {
        getListMyRecruitment()
    }

    private fun getListMyRecruitment(page: Int = 1) {
        launch(loading = refreshLoading) {
            recruitmentRepository.getAllMyRecruitment(page).onEach {
                Log.d(TAG, "getListMyRecruitment: $it")
                adapter.submit(it, page)
            }.collect()
        }
    }

    override val onClickDetail: (cvID: Int) -> Unit = {

    }

    override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
        getListMyRecruitment(page)
    }

    override fun onSwipeRefreshData() {
        getListMyRecruitment()
    }
}