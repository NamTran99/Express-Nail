package com.example.nailexpress.views.ui.main.staff.dialogs

import android.app.Application
import android.support.core.livedata.*
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialogFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.DialogSelectServiceBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.showKeyboard
import com.example.nailexpress.extension.visible
import com.example.nailexpress.models.ui.main.Skill
import com.example.nailexpress.repository.GeneralRepository
import com.example.nailexpress.views.ui.main.staff.adapter.ISelectServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.SelectServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectServiceDialog : BaseDialogFragment<DialogSelectServiceBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_select_service

    val viewModel: ServiceVM by activityViewModels()

    override fun initView() {
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        val param = dialog?.window?.attributes
        param?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = param

        binding.apply {
            action = viewModel

            viewModel.dissmiss.observe(viewLifecycleOwner) {
                dismissNow()
            }
            viewModel.customLoading.observe(viewLifecycleOwner) {
                progressBar.visible(it)
            }
        }
    }

    override fun onResume() {
        binding.etSearch.showKeyboard()
        super.onResume()
    }
}
@HiltViewModel
class ServiceVM @Inject constructor(
    app: Application,
    private val generalRepository: GeneralRepository,
) : BaseViewModel(app), ISelectServiceAdapter {

    var textSearch = MutableStateFlow("")
    var isShowClear = textSearch.map {
        it.isNotEmpty()
    }.asLiveData()
    val listService = MutableLiveData<List<Skill>>()
    val isEmptyData = MutableStateFlow(false)


    val dissmiss: SingleLiveEvent<Any> = SingleLiveEvent()
    val adapter: SelectServiceAdapter by lazy { SelectServiceAdapter(this) }
    val customLoading = LoadingLiveData()

    init {
        getListService(1)
        loadData()
    }

    private fun loadData()= viewModelScope.launch{
        textSearch.onEach {
            if(it == "") isEmptyData.value = false
            getListService(1)
        }.collect()
    }

    private fun getListService(page: Int) = launch(customLoading, isBlockOther = true) {
        textSearch.value.let { search ->
            generalRepository.getListService(search, page).onEach {
                listService.value = it
                if (page == 1) { // NOTE 1
                    adapter.clear()
                }
                adapter.submit(it.map {item ->
                    item.isSKill = true
                    item
                })
                isEmptyData.value = adapter.itemCount == 0
            }.collect()
        }
    }

    fun onClickClearText() {
        textSearch.value = ""
    }

    fun closeDialog() {
        dissmiss.refresh()
    }

    fun onClickSubmit() {
        textSearch.changeValue {
            appEvent.selectedService.value = Skill(name = this)
            dissmiss.value = Any()
        }
    }


    override val onItemSelect = { service: Skill ->
        appEvent.selectedService.value = service
        dissmiss.value = Any()
    }

    override val onLoadMoreListener: ((Int, Int) -> Unit) = { page: Int, _ ->
        getListService(page)
    }

//    val onSearchChange: ((text: String) -> Unit) = {
//        textSearch.value = it
//        getListService(1)
//    }
}