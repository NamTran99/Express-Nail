package com.example.nailexpress.views.ui.nologin

import android.app.Application
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.changeValue
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseRefreshViewModel
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.base.DialogData
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

    val isShowEmpty = SingleLiveEvent<Boolean>()
    var isShowEmptyRecruitment =false
    var isShowEmptyStaff =false

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
                if(page == 1){
                    isShowEmptyStaff = it.isEmpty()
                    checkEmptyData()
                }

                adapterNailStaff.addAll(it, page)
            }.collect()
        }

    private fun checkEmptyData(){
        isTabPost.changeValue {
            if(this){
                isShowEmpty.value = isShowEmptyRecruitment
            }else{
                isShowEmpty.value = isShowEmptyStaff
            }
        }
    }

    private fun getAllRecruitment(page: Int = 1) {
        launch(loading = refreshLoading) {
            recruitmentRepository.getAllRecruitment(page).onEach {
                if(page == 1){
                    isShowEmptyRecruitment = it.isEmpty()
                    checkEmptyData()
                }
                adapterPost.addAll(it, page)
            }.collect()
        }
    }


    override fun onTabChangeListener(index: Int) {
        isTabPost.value = index == HomeStaffViewModel.TAB_POST
        checkEmptyData()
    }

    override fun onSwipeRefreshData() {
        getAllRecruitment()
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
        )
    }

    //load more Post
    private val loadMorePost = object : IPostAction {
        override val onClickDetail: (cvID: Int) -> Unit = {
            showDialogToNavigateToAuthScreen()
        }

        override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
            getAllRecruitment(page)
        }

        override fun onClickApply() {
            showDialogToNavigateToAuthScreen()
        }
    }

    override val onClickBookStaff: (cvID: Int) -> Unit = {
        showDialogToNavigateToAuthScreen()
    }
    override val onClickViewDetail: (id: Int) -> Unit = {
        showDialogToNavigateToAuthScreen()
    }
    override val onLoadMoreListener: (nextPage: Int, pageSize: Int) -> Unit = { page, _ ->
        showDialogToNavigateToAuthScreen()
    }

    private fun showDialogToNavigateToAuthScreen(){
        showDialogConfirm(DialogData(messageID = R.string.dialog_require_login_content, isVisibleCancel = true, callback = {
            navigateToDestination(
                R.id.action_fragmentNoLogin_to_authGraph,
            )
        }))
    }
}