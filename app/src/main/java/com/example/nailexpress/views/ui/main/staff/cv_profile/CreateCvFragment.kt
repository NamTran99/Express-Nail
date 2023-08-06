package com.example.nailexpress.views.ui.main.staff.cv_profile

import android.app.Application
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.changeValue
import android.support.core.livedata.refresh
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.base.ActionTopBarImpl
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.base.IActionTopBar
import com.example.nailexpress.databinding.FragmentCreateCvBinding
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.extension.convertToRequest
import com.example.nailexpress.extension.convertToResult
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.or1
import com.example.nailexpress.models.ui.AppImage
import com.example.nailexpress.models.ui.main.BookServiceForm
import com.example.nailexpress.models.ui.main.CvForm
import com.example.nailexpress.models.ui.main.SearchCityStateForm
import com.example.nailexpress.repository.CvRepository
import com.example.nailexpress.views.ui.main.common.FindWorkingAreaFragment
import com.example.nailexpress.views.ui.main.staff.adapter.BookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IBookServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.IMoreImageAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.MoreImageAdapter
import com.example.nailexpress.views.ui.main.staff.dialogs.SelectServiceDialog
import com.example.nailexpress.views.ui.main.staff.dialogs.SelectServiceDialog.Companion.ARG_TEXT_SEARCH
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CreateCvFragment :
    BaseFragment<FragmentCreateCvBinding, CreateCvVM>(R.layout.fragment_create_cv) {
    companion object {
        const val MAX_SELECTED_IMAGE = 10
    }

    override val viewModel: CreateCvVM by viewModels()

    private val form get() = viewModel.cvForm
    private val moreImage get() = viewModel.cvForm.value?.moreImage ?: mutableListOf()
    private val workingAreaDialog: FindWorkingAreaFragment get() = FindWorkingAreaFragment()
    private val selectServiceDialog: SelectServiceDialog get() = SelectServiceDialog()

    private val avatarResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()
                form.refresh {
                    avatar = (pathImage.getOrNull(0) ?: "").toString()
                    binding.imgImage.setImageUrl(avatar)
                }
            }
        }

    private val listMoreImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val pathImage =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf<Uri>()
                form.refresh {
                    moreImage.clear()
                    moreImage.addAll(
                        pathImage.convertToResult().toMutableList()
                    )
                    viewModel.moreImageAdapter.submit(moreImage)
                }
            }
        }


    override fun initView() {
        binding.action = viewModel

        binding.apply {
            tvWorkingArea.setOnClickListener {
                workingAreaDialog.let {
                    it.arguments = (form.value?.workingAreaForm ?: SearchCityStateForm()).toBundle()
                    it.show(childFragmentManager, FindWorkingAreaFragment.TAG)
                }
            }
            imgImage.setOnClickListener {
                FishBun.with(this@CreateCvFragment)
                    .setImageAdapter(GlideAdapter())
                    .setMaxCount(1)
                    .setActionBarColor(
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        ContextCompat.getColor(requireContext(), R.color.color_primary),
                        true
                    )
                    .setActionBarTitleColor(Color.parseColor("#ffffff"))
                    .startAlbumWithActivityResultCallback(avatarResult)
            }

            viewModel.apply {
                onAddImagesAction = {
                    FishBun.with(this@CreateCvFragment)
                        .setImageAdapter(GlideAdapter())
                        .setMaxCount(MAX_SELECTED_IMAGE)
                        .setMinCount(1)
                        .setSelectedImages(moreImage.convertToRequest())
                        .setActionBarColor(
                            ContextCompat.getColor(requireContext(), R.color.color_primary),
                            ContextCompat.getColor(requireContext(), R.color.color_primary),
                            true
                        )
                        .setActionBarTitleColor(Color.parseColor("#ffffff"))
                        .startAlbumWithActivityResultCallback(listMoreImageResult)
                }

                showDialogSelectService.bind { content ->
                    selectServiceDialog.let {
                        it.arguments = Bundle().apply {
                            putString(ARG_TEXT_SEARCH, content)
                        }
                        it.show(parentFragmentManager, selectServiceDialog.TAG)
                    }
                }
            }

        }
        appEvent.findingWorkingArea.bind {
            form.changeValue {
                binding.apply {
                    txtState.text =
                        it.stateSearch.isBlank() or1 getString(R.string.select_state) or2 it.stateSearch
                    txtCity.text =
                        it.citySearch.isBlank() or1 getString(R.string.select_city) or2 it.citySearch
                }
                workingAreaForm = it
            }
        }
    }
}

@HiltViewModel
class CreateCvVM @Inject constructor(
    application: Application,
    private val cvRepository: CvRepository,
    val appEvent2: AppEvent2
) : BaseViewModel(application), IActionTopBar by ActionTopBarImpl(), IMoreImageAdapter,
    IBookServiceAdapter {
    val cvForm = MutableLiveData(CvForm())

    val onGenderSelect: (pos: Int) -> Unit = {
        cvForm.changeValue {
            gender = it - 1
        }
    }

    val callbackSkillByService = object : IBookServiceAdapter {
        override val onClickRemoveService: (BookServiceForm) -> Unit = {
            cvForm.changeValue {
                listBookSkill.remove(it)
            }
        }
        override val onVisibleRecycler: (Boolean) -> Unit = {

        }
    }
    val moreImageAdapter: MoreImageAdapter by lazy { MoreImageAdapter(this) }
    val adapterSkillByTime: BookServiceAdapter by lazy { BookServiceAdapter(this) }
    val adapterSkillByService: BookServiceAdapter by lazy {
        BookServiceAdapter(
            callbackSkillByService
        )
    }

    init {
        initTopBarAction(this)
        setTitle(R.string.create_cv)
        collectSelectService()
    }

    companion object {
        private const val MY_CV_POSITION = 0

        const val SelectServiceType = 0
        const val SelectTimeType = 1
    }

    //region Chọn service
    val showDialogSelectService = SingleLiveEvent<String>()
    var selectServiceType = SelectServiceType

    var inputSKillByService = MutableLiveData(BookServiceForm())
    var inputSKillByTime = MutableLiveData(BookServiceForm())

    fun onClickSelectService(content: String, serviceType: Int) {
        selectServiceType = serviceType
        showDialogSelectService.value = content
    }

    private fun collectSelectService() {
        appEvent2.selectedService.drop(1).onEach {
            if (selectServiceType == SelectServiceType) {
                inputSKillByService.value = BookServiceForm(it)
            } else {
                inputSKillByTime.value = BookServiceForm(it)
            }
        }.launchIn(viewModelScope)
    }

    fun onClickAddSkillByService() {
        cvForm.refresh {
            inputSKillByService.refresh {
                saveItem(handleAddItem(false))
                adapterSkillByService.addData(this)
            }
        }
        inputSKillByService.value = BookServiceForm()
    }

    fun onClickAddSkillByTime() {
        cvForm.refresh {
            inputSKillByTime.refresh {
                saveItem(handleAddItem(false))
                adapterSkillByTime.addData(this)
                bookTime.price = price
            }
            inputSKillByTime.value = BookServiceForm(price = bookTime.price)
        }
    }

    fun onClickSubmit() = launch {
        cvForm.value?.apply {
            cvRepository.createCv(this)
        }
    }

    val onSelectTimeType: ((Int) -> Unit) = {
        cvForm.changeValue {
            bookTime.unit = it.toString()
        }
    }

    val onEnableSkillByTime: ((Boolean) -> Unit) = { it ->
        cvForm.refresh {
            isSkillByTime = it
        }
    }

    val onEnableSkillBySkill: ((Boolean) -> Unit) = { it ->
        cvForm.refresh {
            isSkillByService = it
        }
    }

    //endregion
    override var onAddImagesAction: () -> Unit = {}
    override var onRemoveImageAction: (AppImage) -> Unit = {
        cvForm.changeValue {
            moreImage.remove(it)
        }
    }
    override var onItemClickListener: (String) -> Unit = {

    }
    override val onClickRemoveService: (BookServiceForm) -> Unit = {
        cvForm.changeValue {
            listBookTime.remove(it)
        }
    }
    override val onVisibleRecycler: (Boolean) -> Unit = {

    }
}