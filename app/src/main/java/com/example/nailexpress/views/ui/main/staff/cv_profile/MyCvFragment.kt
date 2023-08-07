package com.example.nailexpress.views.ui.main.staff.cv_profile

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentMyCvBinding
import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.extension.clearBackStackAndNavigate
import com.example.nailexpress.models.response.CvDTO
import com.example.nailexpress.views.ui.main.staff.cv_profile.viewmodel.MyCvViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MyCvFragment : BaseFragment<FragmentMyCvBinding, MyCvViewModel>(R.layout.fragment_my_cv) {
    override val viewModel: MyCvViewModel by viewModels()

    override fun initView() {
        binding.topBar.setOnBackPress {
            findNavController().popBackStack()
        }
        binding.btnUpdateCv.setOnClickListener {
//            findNavController().navigate(R.id.authGraph)

            findNavController().setGraph(R.id.authGraph)


//            Log.d("TAG", "initView: ${findNavController().displayName}")
//            findNavController().clearBackStackAndNavigate(R.id.authGraph)
//            navigateToDestination(R.id.authGraph)
//            val role = sharePrefs.get<AppConfig.AppRole>(SharePrefKey.APP_ROLE)
//            if(role  == AppConfig.AppRole.Customer){
//                navigateToDestination(R.id.action_global_authGraph, popUpToDes = R.id.staffGraph, inclusive = true)
//            }else{
//                navigateToDestination(R.id.action_global_authGraph, popUpToDes = R.id.customerGraph, inclusive = true)
//            }
        }
        viewModel.myCvLiveData.observe(viewLifecycleOwner) {
            setUi(it)
        }
    }

    override fun loadData() {
        viewModel.getAllMyCv()
    }

    private fun setUi(cvDTO: CvDTO?) {
        with(binding) {
            groupData.isVisible = cvDTO != null
            layoutContentNoCv.isVisible = cvDTO == null
            if (cvDTO != null) {
                cvPictureProfile.apply {
                    setAvatarUrl(cvDTO.avatar_url)
                    name = cvDTO.fullname
                    gender = cvDTO.gender
                    nickName = null
                }
                workerInfoView.apply {
                    setValueTopLeft(cvDTO.price, cvDTO.unit)
                    setValueTopRight(cvDTO.phone)
                    setValueBotLeft(cvDTO.experience_years)
                    setValueBotRight(city = cvDTO.city, state = cvDTO.state)
                }

                serviceWorkerView.apply {
                    setDataListService(cvDTO.skills)
                }

                descriptionView.apply {
                    description = cvDTO.description
                }
            } else {
                btnCreateCv.setOnClickListener {
                    navigateToDestination(com.example.nailexpress.R.id.action_myCvFragment_to_createCvFragment)
                }
            }
        }
    }

}