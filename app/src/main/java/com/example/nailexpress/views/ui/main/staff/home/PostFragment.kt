package com.example.nailexpress.views.ui.main.staff.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nailexpress.base.PageRecyclerAdapter
import com.example.nailexpress.databinding.FragmentPostBinding
import com.example.nailexpress.views.ui.main.staff.HomeStaff
import com.example.nailexpress.views.ui.main.staff.adapter.PostAdapter

open class PostFragment(val adapter:  PageRecyclerAdapter<*,*>? = null) : Fragment() {
    lateinit var binding: FragmentPostBinding
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPostBinding.inflate(layoutInflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    open fun setUp() {
        binding.rvPost.adapter = adapter ?: (parentFragment as HomeStaff).viewModel.adapterPost
    }
}