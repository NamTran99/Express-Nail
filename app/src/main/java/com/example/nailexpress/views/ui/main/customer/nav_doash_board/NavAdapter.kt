package com.example.nailexpress.views.ui.main.customer.nav_doash_board

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nailexpress.views.ui.main.customer.HomeCustomerFragment
import com.example.nailexpress.views.ui.main.customer.MyPostFragment
import com.example.nailexpress.views.ui.main.customer.NotificationFragment
import com.example.nailexpress.views.ui.main.profile.ProfileFragment

class NavAdapter(fragment : Fragment,val list: List<Fragment>) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = BOTTOM_NAV_COUNT

    override fun createFragment(position: Int) = list[position]

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    companion object{
        const val BOTTOM_NAV_COUNT = 4
        const val INDEX_O = 0
        const val INDEX_1 = 1
        const val INDEX_2 = 2
        const val INDEX_3 = 3
    }
}