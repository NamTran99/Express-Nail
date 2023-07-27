package com.example.nailexpress.views.ui.main.mysalon

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentMySalonBinding
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.views.ui.main.mysalon.viewmodel.MySalonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySalonFragment : BaseFragment<FragmentMySalonBinding, MySalonViewModel>(R.layout.fragment_my_salon) {
    override val viewModel: MySalonViewModel by viewModels()

    override fun initView() {
        with(binding) {
            topBar.setOnBackPress {
                findNavController().popBackStack()
            }

            viewModel.salonData.observe(viewLifecycleOwner) {
                setUi(salon = it)
            }

        }
    }

    private fun setUi(salon: Salon?) {
        if (salon == null) {
            binding.layoutContent.isVisible = false
        } else {
            binding.layoutContent.isVisible = true
            with(binding) {
                shopInfoView.apply {
                    shopName = salon.name
                    phoneNumber = salon.phoneDisplay
                    location = salon.address
                }
                salonActiveTime.apply {
                    iilValue = salon.experience_years_display
                }

                majorityCustomerView.apply {
                    iilValue = salon.skinColorDisplay
                }

                workerAccommodation.apply {
                    iilValue = salon.display_have_place
                }

                shuttleBusWorker.apply {
                    iilValue = salon.display_have_car
                }

                salonDescriptionView.apply {
                    description = salon.description
                }

                salonPictureView.apply {
                    setListPicture(salon.listImage)
                }
            }
        }
    }
}