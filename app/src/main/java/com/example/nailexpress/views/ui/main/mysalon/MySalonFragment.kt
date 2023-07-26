package com.example.nailexpress.views.ui.main.mysalon

import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentMySalonBinding
import com.example.nailexpress.views.ui.main.mysalon.viewmodel.MySalonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySalonFragment : BaseFragment<FragmentMySalonBinding, MySalonViewModel>(R.layout.fragment_my_salon) {
    override val viewModel: MySalonViewModel by viewModels()

    override fun initView() {
    }
}