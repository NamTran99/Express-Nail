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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.nailexpress.R
import com.example.nailexpress.app.AppSettingsOwner
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.datasource.local.SharePrefs
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

    @Inject
    lateinit var sharePrefs: SharePrefs

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
        loadData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        appActivity.reloadData = {
            viewModel.loadDataScreen()
        }
        viewModel.loadDataScreen()
        jobEventReceiver = lifecycleScope.launch {
            viewModel.eventReceiver.collectLatest {
                when (it) {
                    is AppEvent.OnNavigation -> navigateToDestinationWithAnim(
                        it.destination,
                        bundle = it.bundle,
                        popUpToDes = it.popUpTo,
                        inclusive = it.isInclusive
                    )
                    is AppEvent.OnNavigationNav -> navigateToDestinationWithAnim(
                        it.nav,
                        popUpToDes = it.popUpTo,
                        inclusive = it.isInclusive
                    )
                    AppEvent.OnCloseApp -> closeApp()
                    AppEvent.OnBackScreen -> onBackPress()
                    is AppEvent.OnShowToast -> toast(it.content)
                    is AppEvent.OnOpenAlertDialog -> showAlertDialog(
                        it.data
                    )
                    AppEvent.RestartActivity -> requireActivity().recreate()
                    else -> {}
                }
            }
        }
    }

    abstract fun initView()
    open fun loadData() {}

    open fun navigateToDestinationWithAnim(
        destination: Int,
        bundle: Bundle? = null,
        inclusive: Boolean = false,
        popUpToDes: Int? = null
    ) {
        val navOptionBuilder = navOptions{
            anim{
                enter= R.anim.enter_anim
                exit = R.anim.exit_anim
                popEnter = R.anim.pop_enter_anim
                popExit = R.anim.pop_exit_anim
            }
            popUpToDes?.let {
                popUpTo(it){
                    this.inclusive = inclusive
                }
            }
        }

        findNavController().apply {
            navigate(destination, bundle, navOptions = navOptionBuilder)
        }
    }

    open fun navigateToDestinationWithAnim(
        nav: NavDirections, inclusive: Boolean = false,
        popUpToDes: Int? = null
    ) {
        Log.d(TAG, "navigateToDestination: ")
        val navOptionBuilder =NavOptions.Builder()
            .setEnterAnim(R.anim.enter_anim)
            .setExitAnim(R.anim.exit_anim)
            .setPopEnterAnim(R.anim.pop_enter_anim)
            .setPopExitAnim(R.anim.pop_exit_anim)

        popUpToDes?.let {
            navOptionBuilder
                .setPopUpTo(destinationId = it, inclusive = inclusive)
        }
        findNavController().navigate(nav, navOptions = navOptionBuilder.build())
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

    fun onBackPress() {
        findNavController().popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        jobEventReceiver?.cancel()
    }
}