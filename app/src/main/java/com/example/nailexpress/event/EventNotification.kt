package com.example.nailexpress.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.views.ui.main.customer.HomeCustomerFragment
import com.example.nailexpress.views.ui.main.customer.NotificationFragment
import com.example.nailexpress.views.ui.main.staff.HomeStaff
import com.example.nailexpress.views.ui.main.staff.NotificationStaff
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class EventNotification : FragmentLifecycleCallbacks() {
    private val TAG = this.javaClass.simpleName
    private var homeFragment: Fragment? = null
    private var fragment: Fragment? = null

    init {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        EventBus.getDefault().register(this)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        fragment = f

        if (f is HomeCustomerFragment || f is HomeStaff) {
            homeFragment = f
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun autoReloadFragmentWhenReceiveNotificationFromServer(autoReloadFragment: AutoReloadFragment){
        fragment?.let {
            (it as? BaseFragment<*,*>)?.let {
                if(it.isVisible){
                    it.viewModel.loadDataScreen()
                }
            }
            (it as? BaseRefreshFragment<*,*>)?.let {
                it.viewModel.onSwipeRefreshData()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateTextNotification(data: NumberNotification) {
        homeFragment?.let {
            (it as? HomeCustomerFragment)?.updateTextNotification(data.numberNotification ?: "")
            (it as? HomeStaff)?.updateTextNotification(data.numberNotification ?: "")
        }
    }
}