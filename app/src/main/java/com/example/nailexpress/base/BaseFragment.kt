package com.example.nailexpress.base

import android.os.Bundle
import android.support.core.extensions.LifecycleSubscriberExt
import android.support.core.route.ActivityResultRegister
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.nailexpress.app.AppSettingsOwner
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.views.dialog.ConfirmDialogOwner
import com.example.nailexpress.views.dialog.loading.LoadingDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel>(val layoutId: Int) :
    Fragment(),
    LifecycleSubscriberExt,
    ActivityResultRegister,
    ConfirmDialogOwner,
    IActionBaseIPLM,
    AppSettingsOwner {
    val loadingDialog by lazy { LoadingDialog(requireContext(), this) }
    val mWindowManager: WindowManager by lazy { requireActivity().windowManager }
    protected var jobEventReceiver: Job? = null
    val self get() = this

    lateinit var binding: T
        private set
    abstract val viewModel: VM

    @Inject
    lateinit var appEvent: AppEvent2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: $TAG")
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDataWhenResumse()
        loadData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        jobEventReceiver = lifecycleScope.launch {
            viewModel.eventReceiver.collectLatest {
                when (it) {
                    is AppEvent.OnNavigation -> navigateToDestination(it.destination, it.bundle)
                    is AppEvent.OnNavigationNav -> navigateToDestination(it.nav)
                    AppEvent.OnCloseApp -> closeApp()
                    AppEvent.OnBackScreen -> onBackPress()
                    is AppEvent.OnShowToast -> toast(it.content)
                    is AppEvent.OnOpenAlertDialog -> showAlertDialog(
                        it.data
                    )

                    else -> {}
                }
            }
        }
    }

    abstract fun initView()
    open fun loadData() {}

    open fun navigateToDestination(destination: Int, bundle: Bundle? = null) {
        Log.d(TAG, "navigateToDestination: ")
        findNavController().apply {
            bundle?.let {
                navigate(destination, it)
            } ?: navigate(destination)
        }
    }

    open fun navigateToDestination(nav: NavDirections) {
        Log.d(TAG, "navigateToDestination: ")
        findNavController().navigate(nav)
    }


    fun toast(@StringRes res: Int) = appActivity.toast(res)

    fun toast(text: String) = appActivity.toast(text)

    fun success(msg: String, time: Int? = Toast.LENGTH_SHORT) {
        appActivity.success(msg, time)
    }

    fun success(@StringRes msg: Int, time: Int? = Toast.LENGTH_SHORT) {
        appActivity.success(msg, time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Fragment Tracer", "Go into ${this.javaClass.name}")
    }

    private fun onBackPress() {
        findNavController().popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        jobEventReceiver?.cancel()
    }
}