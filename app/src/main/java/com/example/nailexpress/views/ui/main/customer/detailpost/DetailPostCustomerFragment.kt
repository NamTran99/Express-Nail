package com.example.nailexpress.views.ui.main.customer.detailpost

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseRefreshFragment
import com.example.nailexpress.databinding.FragmentDetailPostCustomerBinding
import com.example.nailexpress.models.response.RecruitmentDataDTO
import com.example.nailexpress.views.ui.main.customer.detailpost.viewmodel.DetailPostCustomerVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPostCustomerFragment : BaseRefreshFragment<FragmentDetailPostCustomerBinding, DetailPostCustomerVM>(
    R.layout.fragment_detail_post_customer
) {
    override val viewModel: DetailPostCustomerVM by viewModels()
    private val detailPostCustomerFragmentArgs by navArgs<DetailPostCustomerFragmentArgs>()

    override fun loadData() {
        super.loadData()
        viewModel.getRecruitmentById(detailPostCustomerFragmentArgs.recruimentId)
    }

    override fun initView() {
        viewModel.data.observe(viewLifecycleOwner) {
            setUiData(it)
        }
        with(binding) {
            topBar.apply {
                setOnBackPress { }
            }
        }
    }

    private fun setUiData(recruitmentDataDTO: RecruitmentDataDTO) {
        with(binding) {
            userInfoDetailPostView.apply {
                setUserImg(recruitmentDataDTO.image_url)
                setUserId(recruitmentDataDTO.id)
                setContent(recruitmentDataDTO.title)
                setStatus(recruitmentDataDTO.status.toString())
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
                    price = recruitmentDataDTO.price,
                    unit = recruitmentDataDTO.unit
                )
                setTime(
                    icon = AppCompatResources.getDrawable(context, R.drawable.ic_clock),
                    title = context.getString(R.string.time),
                    value = recruitmentDataDTO.booking_time
                )
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