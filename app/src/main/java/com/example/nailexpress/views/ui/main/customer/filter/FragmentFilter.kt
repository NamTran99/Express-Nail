package com.example.nailexpress.views.ui.main.customer.filter

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentFilterCustomerBinding
import com.example.nailexpress.views.widgets.CustomFilterView

class FragmentFilterCustomer :
    BaseFragment<FragmentFilterCustomerBinding, FilterCustomerViewModel>(R.layout.fragment_filter_customer) {
    override val viewModel: FilterCustomerViewModel by viewModels()
    override fun initView() {
        binding.filterView.setAction(viewModel)
    }
}

class FilterCustomerViewModel(application: Application) : BaseViewModel(application),
    CustomFilterView.IActionCustomFilter {

    override fun onClickCity() {
    }

    override fun onClickExpression() {
    }

    override fun onClickGender() {
    }

    override fun onClickState() {
    }

    override fun onClickClose() {

    }
}