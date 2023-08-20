package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentStaffDetailBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.ui.main.staff.adapter.DetailServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailStaffFragment() :
    BaseFragment<FragmentStaffDetailBinding, DetailStaffVM>(layoutId = R.layout.fragment_staff_detail) {

    override val viewModel: DetailStaffVM by viewModels()
    override fun initView() {
        viewModel.getDetailStaff(arguments?.getInt(Constant.STAFF_ID).safe())
        binding.apply {
            action = viewModel

            viewModel.detailCV.bind{
                tvTimeTitle.text = getString(R.string.list_service_by_time, it.priceFormat)
            }
        }

    }

}

@HiltViewModel
class DetailStaffVM @Inject constructor(app: Application, val cvRepository: CvRepository) :
    BaseViewModel(app), IActionTopBar by ActionTopBarImpl() {

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_detail_staff))
    val detailCV = MutableLiveData<Cv>()
    var cvId: Int = 0

    init {
        initTopBarAction(this)
    }

    val skillByServiceAdapter = DetailServiceAdapter()
    val skillByTimeAdapter = DetailServiceAdapter()
    val adapter = DetailServiceAdapter()

    fun getDetailStaff(id: Int) = launch {
        cvRepository.getCvDetail(id).onEach {
            cvId = it.id
            detailCV.value = it

            skillByServiceAdapter.submit(it.listSkillByService)
            skillByTimeAdapter.submit(it.listSkillByTime)
        }.collect()
    }

    fun onClickBookNow() {
        navigateToDestination(
            R.id.action_detailStaffFragment_to_bookNowStaffFragment,
            bundle = bundleOf(Constant.CV_ID to cvId, Constant.IS_BOOK_NOW to true)
        )
    }

    fun onClickBookLater() {
        navigateToDestination(
            R.id.action_detailStaffFragment_to_bookNowStaffFragment,
            bundle = bundleOf(Constant.CV_ID to cvId, Constant.IS_BOOK_NOW to false)
        )
    }
}

