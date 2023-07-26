package com.example.nailexpress.views.ui.main.mysalon.viewmodel

import android.app.Application
import com.example.nailexpress.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MySalonViewModel @Inject constructor(application: Application) : BaseViewModel(application) {
}