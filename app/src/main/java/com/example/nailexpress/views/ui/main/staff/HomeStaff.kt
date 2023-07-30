package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.databinding.FragmentHomeStaffBinding
import com.example.nailexpress.extension.bind
import com.example.nailexpress.extension.drawableClickRight
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
import com.example.nailexpress.views.ui.main.staff.nav_staff.NavDashboardStaff
import com.example.nailexpress.views.widgets.CustomHeaderHome
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeStaff :
    BaseRefreshFragment<FragmentHomeStaffBinding, HomeStaffViewModel>(R.layout.fragment_home_staff),
    CustomHeaderHome.IActionHeader {
    private val listFragment : List<Fragment> = listOf(PostFragment(), CvFragment())

    override val viewModel: HomeStaffViewModel by viewModels()

    override fun initView() {
        binding.apply {
            vm = viewModel
            with(vpMain){
                if(vpMain.adapter == null){
                    vpMain.adapter = NavAdapter(this@HomeStaff, listFragment)
                }
                isSaveEnabled = true
                isUserInputEnabled = false

                viewModel.isTabPost.observe(viewLifecycleOwner){
                    currentItem = if(it) TAB_POST else TAB_CV
                }
            }

            with(header){
                updateAction(this@HomeStaff)
                updateTextNotification("3")
            }
        }
    }

    override fun onTextChange(string: String) {

    }

    override fun onClickNotification() {
        (parentFragment as? NavDashboardStaff)?.tabNotificationClick()
    }

    override fun onClickFilter() {
        navigateToDestination(R.id.filterFragment)
    }

    companion object{
        private const val TAB_POST = 0
        private const val TAB_CV = 1
        private const val TAB_NOTIFI = 2
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