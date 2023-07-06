package com.example.nailexpress.views.ui.main.staff.dialogs

import android.app.Application
import android.support.core.livedata.SingleLiveEvent
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialogFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.DialogSelectServiceBinding
import com.example.nailexpress.extension.launch
import com.example.nailexpress.extension.visible
import com.example.nailexpress.models.ui.main.Service
import com.example.nailexpress.repository.GeneralRepository
import com.example.nailexpress.utils.ViewModelHandleUtils
import com.example.nailexpress.views.ui.main.staff.adapter.ISelectServiceAdapter
import com.example.nailexpress.views.ui.main.staff.adapter.SelectServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@AndroidEntryPoint
class SelectServiceDialog : BaseDialogFragment<DialogSelectServiceBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_select_service

    val viewModel: ServiceVM by activityViewModels()

    override fun initView() {
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        val param  = dialog?.window?.attributes
        param?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = param

        binding.apply {
            action = viewModel

            viewModel.dissmiss.observe(viewLifecycleOwner){
                dismissNow()
            }
            ViewModelHandleUtils.isLoading.observe(viewLifecycleOwner){
                progressBar.visible(it)
            }
        }
    }

}
@HiltViewModel
class ServiceVM @Inject constructor(
    app: Application,
    private val generalRepository: GeneralRepository,
) : BaseViewModel(app), ISelectServiceAdapter {

    init {
        getListService(1)
    }

    var textSearch = ""
    val dissmiss: SingleLiveEvent<Any> = SingleLiveEvent()
    val listService = MutableLiveData<List<Service>>()
    val adapter: SelectServiceAdapter by lazy { SelectServiceAdapter(this) }
    val isEmptyData = MutableLiveData(true)

    private fun getListService(page: Int) = launch {
        generalRepository.getListService(textSearch, page).onEach {
            listService.value = it
            if (page == 1) {
                adapter.clear()
                isEmptyData.value = it.isEmpty()
            }
            adapter.submit(it)
        }.collect()
    }

    override val onItemSelect= {service: Service ->
        appEvent.selectedService.value = service
        dissmiss.value = Any()
    }

    override val onLoadMoreListener: ((Int, Int) -> Unit) = { page: Int, _ ->
        getListService(page)
    }

    val onSearchChange: ((text: String) -> Unit) = {
        textSearch = it
        getListService(1)
    }
}