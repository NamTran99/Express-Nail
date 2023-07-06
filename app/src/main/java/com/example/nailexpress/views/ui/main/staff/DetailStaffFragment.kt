package com.example.nailexpress.views.ui.main.staff

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentStaffDetailBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.models.ui.main.Cv
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.views.ui.main.staff.adapter.DetailServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailStaffFragment() : BaseFragment<FragmentStaffDetailBinding, DetailStaffVM>(layoutId = R.layout.fragment_staff_detail){

    private val args: DetailStaffFragmentArgs by navArgs()
    override val viewModel: DetailStaffVM by viewModels()
    override fun initView() {
        binding.apply {
            action = viewModel
            viewModel.getDetailStaff(args.staffId)
        }
    }

}

@HiltViewModel
class DetailStaffVM @Inject constructor(app: Application, val cvRepository: CvRepository): BaseViewModel(app), IActionTopBar{

    override val title: MutableLiveData<String>
        get() = MutableLiveData(getString(R.string.title_detail_staff))
    val detailCV = MutableLiveData<Cv>()

    val adapter = DetailServiceAdapter()

    fun getDetailStaff(id: Int) = launch{
        cvRepository.getCvDetail(id).onEach {
            detailCV.value = it
            adapter.submit(it.listSkill)
        }.collect()
    }

    fun onClickBookNow(){
        navigateToDestination(R.id.createSalonFragment)

    }

    fun onClickBookLater(){
    }
}

