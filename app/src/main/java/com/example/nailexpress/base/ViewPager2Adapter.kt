package com.example.nailexpress.base

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

abstract class ViewPager2Adapter(
	private val viewPager: ViewPager2,
	fragment: Fragment
) : FragmentStateAdapter(fragment) {
	private var mTabLayout: TabLayout? = null
	var mRoute: List<Fragment>? = null

	init {
		viewPager.adapter = this
	}

	fun submit(items: List<Fragment>?) {
		mRoute = items
	}

	fun setupPageChangeWith(tabLayout: TabLayout) {
		mTabLayout = tabLayout
		(0 until itemCount).forEach {
			val tab = tabLayout.newTab()
			onBindTab(it, tab)
			tabLayout.addTab(tab)
		}
		tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
			override fun onTabSelected(tab: TabLayout.Tab?) {
				tab ?: return
				if (viewPager.currentItem == tab.position) return
				viewPager.currentItem = tab.position
			}

			override fun onTabUnselected(tab: TabLayout.Tab?) {
			}

			override fun onTabReselected(tab: TabLayout.Tab?) {
			}
		})
		viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				if (tabLayout.selectedTabPosition == position) return
				tabLayout.getTabAt(position)?.select()
			}
		})
	}

	protected abstract fun onBindTab(position: Int, tab: TabLayout.Tab)
}