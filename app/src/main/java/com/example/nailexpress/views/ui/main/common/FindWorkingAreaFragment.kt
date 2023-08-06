package com.example.nailexpress.views.ui.main.common

import android.app.Application
import android.app.Dialog
import android.support.core.event.LiveDataStatusOwner
import android.support.core.event.WindowStatusOwner
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.combine1
import android.support.core.livedata.post
import android.support.core.route.argument
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.example.nailexpress.R
import com.example.nailexpress.base.BaseDialogFragment
import com.example.nailexpress.base.BaseFragment
import com.example.nailexpress.base.BaseViewModel
import com.example.nailexpress.databinding.FragmentFindWorkingAreaBinding
import com.example.nailexpress.extension.*
import com.example.nailexpress.models.ui.main.CityDTO
import com.example.nailexpress.models.ui.main.SearchCityStateForm
import com.example.nailexpress.models.ui.main.SearchState
import com.example.nailexpress.models.ui.main.StateDTO
import com.example.nailexpress.repository.GeneralRepository
import com.example.nailexpress.views.ui.main.common.adapter.SelectCityAdapter
import com.example.nailexpress.views.ui.main.common.adapter.SelectStateAdapter
import com.example.nailexpress.views.widgets.setEnable
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FindWorkingAreaFragment(override val layoutId: Int = R.layout.fragment_find_working_area) :
    BaseDialogFragment<FragmentFindWorkingAreaBinding>() {

    companion object{
        val TAG: String = this::class.java.name
    }

     val viewModel: FindWorkingAreaVM by viewModels()

    private val args by lazy {argument<SearchCityStateForm>()}
    private val form: SearchCityStateForm get() = viewModel.formSearch
    private lateinit var stateAdapter: SelectStateAdapter
    private lateinit var cityAdapter: SelectCityAdapter
    private var stateSearch: SearchState = SearchState.STATE
        set(value) {
            field = value

            binding.apply {
                lvChangeData.hide(value == SearchState.STATE)
                btChangeState.hide(value == SearchState.STATE)
                btChangeCity.show(value == SearchState.COMPLETE)
                etSearch.setEnable(value != SearchState.COMPLETE)
                dividerChangeData.show(value == SearchState.COMPLETE)
                rvStates.show(value == SearchState.STATE)
                rvCities.show(value == SearchState.CITY)
                btSubmit.hide(value == SearchState.STATE)
                tvReset.show(value == SearchState.COMPLETE)
                btClose.hide(value == SearchState.COMPLETE)

                when (value) {
                    SearchState.STATE -> {
                        form.clearAll()
                        etSearch.clearText()
                        viewModel.filter("")
                        etSearch.setHint(R.string.search_by_state)
                    }
                    SearchState.CITY -> {
                        form.clearCity()
                        etSearch.clearText()
                        viewModel.filter("")
                        etSearch.setHint(R.string.search_by_city)
                    }
                    SearchState.COMPLETE -> {
                        etSearch.setText(form.format)
                    }
                }
            }
        }

    override fun initView() {
        viewModel.formSearch = args

        binding.apply {
            stateSearch = args.getStateSearch()

            btChangeState.text = form.stateFormat
            btChangeCity.text = form.citySearch
            viewModel.getListState()
            viewModel.getListCity()

            etSearch.bind {
                btClose.show(it.isNotEmpty() && stateSearch != SearchState.COMPLETE)
                viewModel.filter(it.trim())
            }

            stateAdapter = SelectStateAdapter(rvStates).apply {
                onClickItemState = {
                    btChangeState.text = form.setSate(it)
                    viewModel.getListCity()
                    stateSearch = SearchState.CITY
                }
            }

            cityAdapter = SelectCityAdapter(rvCities).apply {
                onClickItem = {
                    btChangeCity.text = form.setCity(it)
                    stateSearch = SearchState.COMPLETE
                }
            }
            btBack.onClick {
                dismiss()
            }
            btClose.onClick {
                etSearch.setText("")
            }
            tvReset.onClick {
                form.clearAll()
                stateSearch = SearchState.STATE
            }
            btChangeState.onClick {
                form.clearAll()
                stateSearch = SearchState.STATE
            }
            btChangeCity.onClick {
                form.clearCity()
                stateSearch = SearchState.CITY
            }
            btSubmit.onClick {
                dismiss()
                appEvent.findingWorkingArea.post(
                    form
                )
            }
            viewModel.apply {
                litStateResult.observe(viewLifecycleOwner) {
                    stateAdapter.submit(it)
                    (it.isEmpty() && stateSearch == SearchState.STATE).show(
                        listOf(
                            lvEmptyData
                        )
                    )
                }

                listCityResult.observe(viewLifecycleOwner) {
                    cityAdapter.submit(it)
                    (it.isEmpty() && stateSearch == SearchState.CITY).show(
                        listOf(
                            lvEmptyData
                        )
                    )
                }
            }
        }
    }
}

@HiltViewModel
class FindWorkingAreaVM @Inject constructor(
    app: Application,
    private val repo: GeneralRepository,
) : BaseViewModel(app)
   {

    var formSearch = SearchCityStateForm()

    val liveFilter = MutableLiveData("")
    var listStateAllResult = SingleLiveEvent<List<StateDTO>>()
    val listCityAllResult = SingleLiveEvent<List<CityDTO>>()

    val litStateResult = listStateAllResult.combine1(liveFilter) { list, filter ->
        list.filter { it.state_code.lowerCaseContain(filter) || it.state.lowerCaseContain(filter) }
    }
    val listCityResult = listCityAllResult.combine1(liveFilter) { list, filter ->
        list.filter { it.city.lowerCaseContain(filter) }
    }

    fun filter(filter: String) {
        liveFilter.post(filter)
    }

    fun getListState() = launch {
        repo.getListState().onEach {
            listStateAllResult.postValue(it)
        }.collect()
    }

    fun getListCity() = launch {
        if (formSearch.stateSearch.isBlank()) return@launch
        repo.getListCity(formSearch.stateSearch).onEach {
            listCityAllResult.postValue(it)
        }.collect()
    }
}