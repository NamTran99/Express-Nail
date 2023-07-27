package com.example.nailexpress.views.ui.main.staff.home

import com.example.nailexpress.views.ui.main.staff.HomeStaff

class CvFragment : PostFragment() {
    override fun setUp() {
        binding.rvPost.adapter = (parentFragment as HomeStaff).viewModel.adapterCv
    }
}