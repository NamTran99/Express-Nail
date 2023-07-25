package com.example.nailexpress.views.ui.main.nav_doash_board

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.databinding.NavDashBoardBinding
import com.example.nailexpress.views.ui.main.nav_doash_board.NavAdapter.Companion.INDEX_1
import com.example.nailexpress.views.ui.main.nav_doash_board.NavAdapter.Companion.INDEX_2
import com.example.nailexpress.views.ui.main.nav_doash_board.NavAdapter.Companion.INDEX_3
import com.example.nailexpress.views.ui.main.nav_doash_board.NavAdapter.Companion.INDEX_O

class NavDashBoard : Fragment() {
    private lateinit var binding: NavDashBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return NavDashBoardBinding.inflate(inflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listener()
    }

    private fun listener() {
        binding.apply {
            fabClientCheckIn.setOnClickListener {
                findNavController().navigate(R.id.createRecruitmentFragment)
            }

            bottomNavigation.setOnItemSelectedListener {
                vpMain.currentItem = when (it.itemId) {
                    R.id.homeCustomerFragment -> INDEX_O
                    R.id.navPost -> INDEX_1
                    R.id.navNoti -> INDEX_2
                    else -> INDEX_3
                }
                true
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        binding.vpMain.apply {
            offscreenPageLimit = NavAdapter.BOTTOM_NAV_COUNT
            isSaveEnabled = true
            if (adapter == null) {
                adapter = NavAdapter(this@NavDashBoard)
            }
            isUserInputEnabled = false
        }
    }
}