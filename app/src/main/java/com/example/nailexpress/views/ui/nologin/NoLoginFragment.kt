package com.example.nailexpress.views.ui.nologin

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.databinding.FragmentNoLoginBinding
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavAdapter
import com.example.nailexpress.views.ui.main.staff.home.PostFragment
import com.example.nailexpress.views.widgets.CustomHeaderHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoLoginFragment :
    BaseRefreshFragment<FragmentNoLoginBinding, NoLoginViewModel>(R.layout.fragment_no_login),
    CustomHeaderHome.IActionHeader {
    override val viewModel: NoLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkIsLogin()
    }

    override fun initView() {
        binding.apply {
            vm = viewModel
            with(vpMain) {
                if (adapter == null) {
                    adapter = NavAdapter(
                        this@NoLoginFragment, listOf(
                            PostFragment(viewModel.adapterPost),
                            PostFragment(viewModel.adapterNailStaff)
                        )
                    )
                }
                isSaveEnabled = true
                isUserInputEnabled = false

                viewModel.isTabPost.observe(viewLifecycleOwner) {
                    currentItem = if (it) TAB_POST else TAB_CV
                }
            }

            with(header) {
                updateAction(this@NoLoginFragment)
                updateTextNotification("")
            }
        }
    }

    override fun onTextChange(string: String) {
//        navigateToDestinationWithAnim(
//            R.id.action_fragmentNoLogin_to_authGraph,
//        )
    }

    override fun onClickNotification() {
        //không có notification tại màn này
    }

    override fun onClickFilter() {
        navigateToDestinationWithAnim(
            R.id.action_fragmentNoLogin_to_authGraph,
        )
    }

    companion object {
        private const val TAB_POST = 0
        private const val TAB_CV = 1
    }
}