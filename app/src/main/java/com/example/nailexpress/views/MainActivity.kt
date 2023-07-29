package com.example.nailexpress.views

import android.os.Bundle
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.databinding.ActivityMainBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(layoutId = R.layout.activity_main) {

    override val fragmentContainerView = R.id.fragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharePrefs.get<String>(SharePrefKey.TOKEN).safe().isNotBlank()) {
            if(getRole() == AppConfig.AppRole.Customer){
                navigateToDestination(R.id.action_global_customerGraph)
            }else{
                navigateToDestination(R.id.action_global_staff_graph)
            }
        }
    }

    fun getRole() =  sharePrefs.get<AppConfig.AppRole>(SharePrefKey.APP_ROLE)
}