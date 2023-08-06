package com.example.nailexpress.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.nailexpress.datasource.AppEvent2
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.models.ui.base.DialogData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//interface IVMRefreshStatus {
//    val refreshLoading: LoadingEvent get() = LoadingLiveData()
//}


interface IActionTopBar {
    val title: MutableLiveData<String>
    val search: MutableLiveData<String>
    val isShowBackButton: MutableLiveData<Boolean>
    val onSearchTextChange: ((String) -> Unit)
    val context: Application
    var self: BaseViewModel?

    fun onBackClick() {
        self?.backScreen()
    }

    fun setTitle(id: Int) {
        title.value = context.getString(id)
    }

    fun initTopBarAction(vm: BaseViewModel)
}

class ActionTopBarImpl() : IActionTopBar {
    override val title = MutableLiveData("")
    override val search = MutableLiveData("")
    override val isShowBackButton = MutableLiveData(true)
    override val onSearchTextChange: ((String) -> Unit) get() = {}
    override val context: Application = MainApplication.application
    override var self: BaseViewModel? = null

    override fun initTopBarAction(vm: BaseViewModel) {
        self = vm
    }
}

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val TAG by lazy { this::class.java.name }
    protected val evenSender = Channel<AppEvent>()

    val eventReceiver = evenSender.receiveAsFlow().conflate()

    @Inject
    lateinit var appLocal: SharePrefs

    @Inject
    lateinit var appEvent: AppEvent2

    open fun loadDataScreen() {

    }

    open fun onClickClose() {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnCloseApp)
        }
    }

    open fun showDialogConfirm(
        data: DialogData
    ) {
        viewModelScope.launch {
            evenSender.send(AppEvent.OnOpenAlertDialog(data))
        }
    }

    open fun getString(idString: Int) = getApplication<Application>().getString(idString)

    open fun getString(idString: Int, str: String) =
        getApplication<Application>().getString(idString, str)

    open fun navigateToDestination(
        action: Int, bundle: Bundle? = null, inclusive: Boolean = false,
        popUpToDes: Int? = null
    ) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnNavigation(action, bundle, popUpToDes, inclusive)
        )
    }

    open fun navigateToDestination(
        nav: NavDirections, inclusive: Boolean = false,
        popUpToDes: Int? = null
    ) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnNavigationNav(nav, popUpToDes, inclusive)
        )
    }

    open fun backScreen() = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnBackScreen
        )
    }

    open fun closeApp() = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnCloseApp
        )
    }

    open fun restartActivity() = viewModelScope.launch {
        evenSender.send(
            AppEvent.RestartActivity
        )
    }

    open fun showToast(content: String) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnShowToast(content)
        )
    }

    open fun showToast(contentID: Int) = viewModelScope.launch {
        evenSender.send(
            AppEvent.OnShowToast(getString(contentID))
        )
    }
}

sealed class AppEvent {
    class OnNavigation(
        val destination: Int, val bundle: Bundle? = null, val popUpTo: Int? = null,
        val isInclusive: Boolean = false
    ) :
        AppEvent()

    class OnNavigationNav(
        val nav: NavDirections,
        val popUpTo: Int? = null,
        val isInclusive: Boolean = false
    ) : AppEvent()

    object OnCloseApp : AppEvent()
    object OnBackScreen : AppEvent()
    class OnShowToast(val content: String, val type: Long = 2000) : AppEvent()
    class OnOpenAlertDialog(
        val data: DialogData
    ) : AppEvent()

    object RestartActivity : AppEvent()
}