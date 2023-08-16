package com.example.nailexpress.views.ui.main.staff.detail_post

import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.databinding.FragmentDetailPostStaffBinding
import com.example.nailexpress.extension.safe
import com.example.nailexpress.models.response.RecruitmentDataDTO
import com.example.nailexpress.utils.KEY_ID_POST_DETAIL
import com.example.nailexpress.views.ui.main.staff.detail_post.viewmodel.DetailPostStaffViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPostStaffFragment : BaseFragment<FragmentDetailPostStaffBinding, DetailPostStaffViewModel>(
    R.layout.fragment_detail_post_staff
) {
    override val viewModel: DetailPostStaffViewModel by viewModels()

    private val id: Int? by lazy {
        arguments?.getInt(KEY_ID_POST_DETAIL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id?.let {
            viewModel.getDetailPostById(it)
        } ?: kotlin.run {
            binding.root.post {
                binding.layoutContent.isVisible = false
            }
        }
    }

    override fun initView() {
        viewModel.dataPostDetail.observe(viewLifecycleOwner) {
            setUi(it)
        }
        with(binding) {
            topBar.setOnBackPress {
                findNavController().popBackStack()
            }
        }
    }

    private fun setUi(recruitmentDataDTO: RecruitmentDataDTO?) {
        binding.layoutContent.isVisible = recruitmentDataDTO != null
        if (recruitmentDataDTO != null) {
            with(binding) {
                userInfoDetailPostView.apply {
                    setUserImg(recruitmentDataDTO.image_url)
                    setUserId(recruitmentDataDTO.id)
                    setContent(recruitmentDataDTO.title)
                    setStatus(recruitmentDataDTO.status)
                    setSalonName(recruitmentDataDTO.contact_name)
                    setDistance(recruitmentDataDTO.distance)
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

                    setListService(recruitmentDataDTO.salary_type ,recruitmentDataDTO.skills)
                    setTimeTitle(recruitmentDataDTO.salary_type ,recruitmentDataDTO.price.safe(), recruitmentDataDTO.unit.safe())

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
    }
}