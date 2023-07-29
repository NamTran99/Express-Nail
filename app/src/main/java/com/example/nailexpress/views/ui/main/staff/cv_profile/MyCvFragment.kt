package com.example.nailexpress.views.ui.main.staff.cv_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentMyCvBinding
import com.example.nailexpress.views.ui.main.staff.cv_profile.viewmodel.MyCvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCvFragment : BaseFragment<FragmentMyCvBinding, MyCvViewModel>(R.layout.fragment_my_cv) {
    override val viewModel: MyCvViewModel by viewModels()

    override fun initView() {
    }

}