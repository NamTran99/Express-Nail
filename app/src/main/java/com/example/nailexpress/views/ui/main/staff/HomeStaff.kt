package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.databinding.FragmentHomeStaffBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.views.ui.main.customer.IActionTabChange
import com.example.nailexpress.views.ui.main.customer.adapter.IMyPostAction
import com.example.nailexpress.views.ui.main.customer.adapter.MyPostAdapter
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IPostAction
import com.example.nailexpress.views.ui.main.staff.adapter.PostAdapter
import com.example.nailexpress.views.ui.main.staff.home.CvFragment
import com.example.nailexpress.views.ui.main.staff.home.PostFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeStaff :
    BaseRefreshFragment<FragmentHomeStaffBinding, HomeStaffViewModel>(R.layout.fragment_home_staff) {
    private val listFragment : List<Fragment> = listOf(PostFragment(), CvFragment())
    private val adapter by lazy { NavAdapter(this, listFragment) }

    override val viewModel: HomeStaffViewModel by viewModels()
    override fun initView() {
        binding.apply {
            vm = viewModel
            vpMain.adapter = adapter

            viewModel.isTabPost.observe(viewLifecycleOwner){
                vpMain.currentItem = if(it) 0 else 1
            }
        }
    }
}

@HiltViewModel
class HomeStaffViewModel @Inject constructor(
    application: Application,
    private val recruitmentRepository: RecruitmentBookingStaffRepository,
    private val cvRepository: CvRepository
) : BaseRefreshViewModel(application), IActionTabChange {
    val isTabPost = MutableLiveData(true)
    val adapterPost by lazy { PostAdapter(loadMorePost) }
    val adapterCv by lazy { MyPostAdapter(lostMoreCV) }

    init {
        getAllRecruitment()
        getListCv()
    }

    override fun onTabChangeListener(index: Int) {
        isTabPost.value = index == TAB_POST
    }

    override fun onSwipeRefreshData() {
        getAllRecruitment()
        getListCv()
    }

    private fun getAllRecruitment(page: Int = 1) {
        launch(loading = refreshLoading) {
            recruitmentRepository.getAllRecruitment(page).onEach {
                adapterPost.submit(it)
            }.collect()
        }
    }

    private fun getListCv(page: Int = 1) {
        launch(loading = refreshLoading) {
            cvRepository.getListCvStaff(page).onEach { e ->
                adapterCv.submit(e.map { it.convertToRecruitmentForm() })
            }.collect()
        }
    }

    //load more Post
    private val loadMorePost = object : IPostAction {
        override val onClickDetail: (cvID: Int) -> Unit = {

        }

        override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
            getAllRecruitment(page)
        }
    }

    private val lostMoreCV = object : IMyPostAction {
        override val onClickDetail: (cvID: Int) -> Unit = {

        }
        override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
            getListCv(page)
        }
    }

    companion object {
        const val TAB_POST = 0
    }
}