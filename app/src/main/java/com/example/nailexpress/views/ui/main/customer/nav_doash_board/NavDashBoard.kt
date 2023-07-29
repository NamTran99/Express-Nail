package com.example.nailexpress.views.ui.main.customer.nav_doash_board

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.databinding.NavDashBoardBinding
import com.example.nailexpress.views.ui.main.customer.HomeCustomerFragment
import com.example.nailexpress.views.ui.main.customer.MyPostFragment
import com.example.nailexpress.views.ui.main.customer.NotificationFragment
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavAdapter.Companion.INDEX_1
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavAdapter.Companion.INDEX_2
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavAdapter.Companion.INDEX_3
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavAdapter.Companion.INDEX_O
import com.example.nailexpress.views.ui.main.profile.ProfileFragment

open class NavDashBoard : Fragment() {
    protected lateinit var binding: NavDashBoardBinding
    open val listItemId = listOf( R.id.homeCustomerFragment,R.id.navPost,R.id.navNoti,R.id.profileFragment)

    open val listItem: List<Fragment> = listOf(
        HomeCustomerFragment(), MyPostFragment(), NotificationFragment(),
        ProfileFragment()
    )

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
                vpMain.currentItem = listItemId.indexOf(it.itemId)
                true
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        binding.vpMain.apply {
            offscreenPageLimit = listItem.size
            isSaveEnabled = true
            if (adapter == null) {
                adapter = NavAdapter(this@NavDashBoard, listItem)
            }
            isUserInputEnabled = false
        }
    }

    fun tabNotificationClick(){
        binding.bottomNavigation.selectedItemId = R.id.navNoti
    }
}