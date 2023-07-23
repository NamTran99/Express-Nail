package com.example.nailexpress.views

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.databinding.ActivityMainBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.extension.hide
import com.example.nailexpress.extension.onClick
import com.example.nailexpress.extension.safe
import com.example.nailexpress.extension.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(layoutId = R.layout.activity_main) {

    override val fragmentContainerView = R.id.fragmentContainerView


    private val listener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            binding.apply {
                when (destination.parent?.id) {
                    R.id.authGraph -> {
                        lvBottom.hide()
                    }
                    else -> {
                        when(destination.id){
                            R.id.homeCustomerFragment, R.id.profileFragment -> lvBottom.show()
                            else -> lvBottom.hide()
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.fabClientCheckIn.onClick{
            navigateToDestination(R.id.createRecruitmentFragment)
        }
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController!!)
        if (sharePrefs.get<String>(SharePrefKey.TOKEN).safe().isNotBlank()) {
            navigateToDestination(R.id.action_global_customerGraph)
        }
    }

    override fun onResume() {
        super.onResume()
        navController?.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController?.removeOnDestinationChangedListener(listener)
    }
}