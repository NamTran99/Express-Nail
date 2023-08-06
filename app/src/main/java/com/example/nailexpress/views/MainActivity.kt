package com.example.nailexpress.views

import com.example.nailexpress.R
import com.example.nailexpress.base.BaseActivity
import com.example.nailexpress.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(layoutId = R.layout.activity_main) {
    override val fragmentContainerView = R.id.fragmentContainerView
}