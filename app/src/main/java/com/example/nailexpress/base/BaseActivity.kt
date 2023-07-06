package com.example.nailexpress.base


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.PersistableBundle
import android.support.core.extensions.LifecycleSubscriberExt
import android.support.core.route.RouteDispatcher
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.nailexpress.app.ResultsLifecycleOwner
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.exception.ErrorHandler
import com.example.nailexpress.exception.ErrorHandlerImpl
import com.example.nailexpress.utils.ViewModelHandleUtils
import com.example.nailexpress.views.dialog.CommonDialog
import com.example.nailexpress.views.dialog.CommonDialogOwner
import com.example.nailexpress.views.dialog.loading.LoadingDialog
import es.dmoral.toasty.Toasty
import javax.inject.Inject

abstract class BaseActivity<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) : AppCompatActivity(),
    LifecycleSubscriberExt,
    RouteDispatcher,
    CommonDialogOwner,
    ResultsLifecycleOwner,
    ErrorHandler by  ErrorHandlerImpl(),
    IActionBaseIPLM{
    private val loadingDialog by lazy { LoadingDialog(this, this) }
    lateinit var binding: VB
        private set
    lateinit var navHostFragment: NavHostFragment
        private set
    var navController: NavController? = null
        private set
    open val fragmentContainerView: Int? = null
    @Inject
    lateinit var sharePrefs: SharePrefs
//    val userLocalSource by inject<UserLocalSource>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resultLifecycle.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        if (fragmentContainerView != null) {
            navHostFragment =
                supportFragmentManager.findFragmentById(fragmentContainerView!!) as NavHostFragment
            navController = navHostFragment.navController
        }
        onBackPressedDispatcher.addCallback(this, object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(navController == null || navController?.popBackStack()?.not() == true){
                    finish()
                }
            }
        })
    }


    private fun handleObserverViewModel() {
        ViewModelHandleUtils.isLoading.bind (loadingDialog::show)
        ViewModelHandleUtils.isError.bind{ handle(this, it) }
    }

    fun toast(@StringRes res: Int) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun success(msg: String, time: Int? = Toast.LENGTH_SHORT) {
        Toasty.success(this, msg, time!!).show()
    }

    fun success(@StringRes msg: Int, time: Int? = Toast.LENGTH_SHORT) {
        val toasty: Toast = Toasty.success(this, getString(msg), time!!)
        toasty.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 220)
        toasty.show()
    }

    // unfocus edittext when select outside
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    open fun navigateToDestination(destination: Int, bundle: Bundle? = null) {
        navController?.apply {
            bundle?.let {
                navigate(destination, it)
            } ?: navigate(destination)
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(intent)
    }

    override fun onResume() {
        handleObserverViewModel()
        super.onResume()
    }

}