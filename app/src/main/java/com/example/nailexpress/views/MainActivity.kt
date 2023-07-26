package com.example.nailexpress.views

import android.os.Bundle
import com.example.nailexpress.R
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
//        if (sharePrefs.get<String>(SharePrefKey.TOKEN).safe().isNotBlank()) {
//            navigateToDestination(R.id.action_global_customerGraph)
//        }
    }
}