package com.example.nailexpress.views.ui.main.staff.cv_profile.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.repository.CvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCvViewModel @Inject constructor(
    application: Application,
    private val cvRepository: CvRepository
) : BaseViewModel(application) {

    val myCvLiveData: MutableLiveData<CvDTO?> = MutableLiveData()

    init {
        getAllMyCv()
    }

    private fun getAllMyCv() {
        viewModelScope.launch {
            cvRepository.getAllMyCv().onEach {
                myCvLiveData.value = it.getOrNull(MY_CV_POSITION)
            }.collect()
        }
    }

    companion object {
        private const val MY_CV_POSITION = 0
    }
}