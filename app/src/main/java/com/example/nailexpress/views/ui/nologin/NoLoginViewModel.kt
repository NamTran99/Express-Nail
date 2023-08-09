package com.example.nailexpress.views.ui.nologin

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.safe
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.repository.RecruitmentBookingStaffRepository
import com.example.nailexpress.views.ui.main.customer.IActionTabChange
import com.example.nailexpress.views.ui.main.customer.adapter.INailStaffAction
import com.example.nailexpress.views.ui.main.customer.adapter.NailStaffAdapter
import com.example.nailexpress.views.ui.main.staff.HomeStaffViewModel
import com.example.nailexpress.views.ui.main.staff.adapter.IPostAction
import com.example.nailexpress.views.ui.main.staff.adapter.PostAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NoLoginViewModel @Inject constructor(
    application: Application,
    private val recruitmentRepository: RecruitmentBookingStaffRepository,
    private val cvRepository: CvRepository,
    private val sharePrefs: SharePrefs
) : BaseRefreshViewModel(application), IActionTabChange, INailStaffAction {
    val isTabPost = MutableLiveData(true)
    val adapterPost by lazy { PostAdapter(loadMorePost) }
    val adapterNailStaff = NailStaffAdapter(this)

    init {
        getAllRecruitment()
        getListStaffNail()
        adapterNailStaff.onLoadMoreListener = { nextPage, _ ->
            getListStaffNail(nextPage)
        }
    }


    private fun getListStaffNail(page: Int = 1, search: String = "") =
        launch(loading = refreshLoading) {
            cvRepository.getListCv(page = page).onEach {
                adapterNailStaff.submit(it, page)
            }.collect()
        }

    override fun onTabChangeListener(index: Int) {
        isTabPost.value = index == HomeStaffViewModel.TAB_POST
    }

    override fun onSwipeRefreshData() {
        getAllRecruitment()
    }

    private fun getAllRecruitment(page: Int = 1) {
        launch(loading = refreshLoading) {
            recruitmentRepository.getAllRecruitment(page).onEach {
                adapterPost.submit(it, page)
            }.collect()
        }
    }

    fun checkIsLogin() {
        if (sharePrefs.get<String>(SharePrefKey.TOKEN).safe().isNotBlank()) {
            if (getRole() == AppConfig.AppRole.Customer) {
                navigateToDestination(
                    R.id.action_fragmentNoLogin_to_customerGraph2,
                    popUpToDes = R.id.fragmentNoLogin,
                    inclusive = true
                )
            } else {
                navigateToDestination(
                    R.id.action_fragmentNoLogin_to_staffGraph,
                    popUpToDes = R.id.fragmentNoLogin,
                    inclusive = true
                )
            }
        }
    }

    fun getRole() = sharePrefs.get<AppConfig.AppRole>(SharePrefKey.APP_ROLE)

    fun startNavLogin() {
        navigateToDestination(
            R.id.action_fragmentNoLogin_to_authGraph,
            inclusive = true,
            popUpToDes = R.id.fragmentNoLogin
        )
    }

    //load more Post
    private val loadMorePost = object : IPostAction {
        override val onClickDetail: (cvID: Int) -> Unit = {
            navigateToDestination(
                R.id.action_fragmentNoLogin_to_authGraph,
                inclusive = true,
                popUpToDes = R.id.fragmentNoLogin
            )
        }

        override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
            getAllRecruitment(page)
        }

        override fun onClickApply() {
            navigateToDestination(
                R.id.action_fragmentNoLogin_to_authGraph,
                inclusive = true,
                popUpToDes = R.id.fragmentNoLogin
            )
        }
    }

    override val onClickBookStaff: (cvID: Int) -> Unit = {
        navigateToDestination(
            R.id.action_fragmentNoLogin_to_authGraph,
            inclusive = true,
            popUpToDes = R.id.fragmentNoLogin
        )
    }
    override val onClickViewDetail: (id: Int) -> Unit = {
        navigateToDestination(
            R.id.action_fragmentNoLogin_to_authGraph,
            inclusive = true,
            popUpToDes = R.id.fragmentNoLogin
        )
    }
    override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
        navigateToDestination(
            R.id.action_fragmentNoLogin_to_authGraph,
            inclusive = true,
            popUpToDes = R.id.fragmentNoLogin
        )
    }
}