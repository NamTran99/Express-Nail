package com.example.nailexpress.views.ui.main.staff.filter

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentFilterBinding
import com.example.nailexpress.views.widgets.CustomFilterView

class FragmentFilter: BaseFragment<FragmentFilterBinding,FilterViewModel>(R.layout.fragment_filter) {
    override val viewModel: FilterViewModel by viewModels()
    override fun initView() {
        binding.filterView.setAction(viewModel)
    }
}

class FilterViewModel(application: Application) : BaseViewModel(application), CustomFilterView.IActionCustomFilter{

    override fun onClickCity() {
        super.onClickCity()
    }

    override fun onClickState() {
        super.onClickState()
    }

    override fun onClickClose() {
        super<CustomFilterView.IActionCustomFilter>.onClickClose()
    }
}