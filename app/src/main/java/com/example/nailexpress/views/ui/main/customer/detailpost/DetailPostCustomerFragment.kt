package com.example.nailexpress.views.ui.main.customer.detailpost

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentDetailPostCustomerBinding
import com.example.nailexpress.extension.show
import com.example.nailexpress.models.response.RecruitmentDataDTO
import com.example.nailexpress.models.ui.main.Salon
import com.example.nailexpress.utils.Constant
import com.example.nailexpress.views.ui.main.customer.detailpost.viewmodel.DetailPostCustomerVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPostCustomerFragment :
    BaseFragment<FragmentDetailPostCustomerBinding, DetailPostCustomerVM>(
        R.layout.fragment_detail_post_customer
    ) {
    override val viewModel: DetailPostCustomerVM by viewModels()

    override fun loadData() {
        super.loadData()
        arguments?.getInt(Constant.RECRUIMENT_ID)?.let { viewModel.getRecruitmentById(it) }
    }

    override fun initView() {
        viewModel.dataRecruitment.observe(viewLifecycleOwner) {
            it.salon_id?.let {
                viewModel.getSalonById(it)
            } ?: kotlin.run {
                setSalonUi(null)
            }
            setUiData(it)
        }

        viewModel.dataSalon.observe(viewLifecycleOwner) {
            setSalonUi(it)
        }
        with(binding) {
            topBar.apply {
                setOnBackPress {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setUiData(recruitmentDataDTO: RecruitmentDataDTO) {
        with(binding) {
            userInfoDetailPostView.apply {
                setUserImg(recruitmentDataDTO.image_url)
                setUserId(recruitmentDataDTO.id)
                setContent(recruitmentDataDTO.title)
                setStatus(recruitmentDataDTO.status)
                setSalonName(recruitmentDataDTO.contact_name)
                setDistance(recruitmentDataDTO.distance)
            }

            statusOfWorkerView.apply {
                setListWorkerApplied(recruitmentDataDTO.applies?.size)
            }

            jobInfoView.apply {
                setLocation(recruitmentDataDTO.address)
                setExperience(
                    icon = AppCompatResources.getDrawable(context, R.drawable.ic_bag_exp),
                    title = context.getString(R.string.experience),
                    value = recruitmentDataDTO.experience_years
                )
                setGender(
                    icon = AppCompatResources.getDrawable(context, R.drawable.ic_user_gender),
                    title = context.getString(R.string.gender),
                    value = recruitmentDataDTO.gender
                )
                setSalary(
                    icon = AppCompatResources.getDrawable(context, R.drawable.ic_salary_dollar),
                    title = context.getString(R.string.salary),
                    salaryType = recruitmentDataDTO.salary_type,
                    price = recruitmentDataDTO.price,
                    unit = recruitmentDataDTO.unit
                )
                setTime(
                    icon = AppCompatResources.getDrawable(context, R.drawable.ic_clock_2),
                    title = context.getString(R.string.time),
                    value = recruitmentDataDTO.booking_time
                )

                setListService(recruitmentDataDTO.skills)
            }
            jobDescriptionView.apply {
                description = recruitmentDataDTO.description
            }
            infoUserBookWorkerView.apply {
                customerName = recruitmentDataDTO.contact_name
                phoneNumber = recruitmentDataDTO.contact_phone
            }
        }
    }

    private fun setSalonUi(salon: Salon?) {
        with(binding) {
            if (salon != null) {
                view1.show()
                shopInfoView.apply {
                    isVisible =
                        salon.name.isNotBlank() && salon.phoneDisplay.isNotBlank() && salon.address.isNotBlank()
                    shopName = salon.name
                    phoneNumber = salon.phoneDisplay
                    location = salon.address
                }

                itemInfoSalonActive.apply {
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
            } else {
                shopInfoView.isVisible = false
                itemInfoSalonActive.isVisible = false
                majorityCustomerView.isVisible = false
                workerAccommodation.isVisible = false
                shuttleBusWorker.isVisible = false
                salonDescriptionView.isVisible = false
                salonPictureView.isVisible = false
            }
        }
    }

}