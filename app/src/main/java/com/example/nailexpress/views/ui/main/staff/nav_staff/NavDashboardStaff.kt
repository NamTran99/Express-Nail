package com.example.nailexpress.views.ui.main.staff.nav_staff

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.views.ui.main.customer.HomeCustomerFragment
import com.example.nailexpress.views.ui.main.customer.MyPostFragment
import com.example.nailexpress.views.ui.main.customer.NotificationFragment
import com.example.nailexpress.views.ui.main.customer.nav_doash_board.NavDashBoard
import com.example.nailexpress.views.ui.main.profile.ProfileFragment
import com.example.nailexpress.views.ui.main.staff.HomeStaff

class NavDashboardStaff : NavDashBoard() {
    override val listItem: List<Fragment> = listOf(
        HomeStaff(), MyPostFragment(), NotificationFragment(),
        ProfileFragment()
    )
    override val listItemId: List<Int> = listOf(
        R.id.homeStaffFragment, R.id.bookingOfMe, R.id.navNoti, R.id.profile
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.bottomNavigation) {
            menu.clear()
            inflateMenu(R.menu.menu_bottom_staff)
        }

        with(binding) {
            fabClientCheckIn.setOnClickListener {
                findNavController().navigate(R.id.action_navDashBoardStaff_to_myCvFragment)
            }
        }
    }
}