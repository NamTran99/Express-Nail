package android.support.core.permission

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.core.extensions.safe
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity

interface PermissionAccessible {
    fun check(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions? = null,
        onPermission: (Boolean) -> Unit
    ): PermissionRequest

    fun checkAny(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions? = null,
        onPermission: (Boolean) -> Unit
    ): PermissionRequest

    fun access(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions? = null,
        onPermission: () -> Unit
    ): PermissionRequest {
        return check(requestCode, *permissions, options = options) {
            if (it) onPermission()
        }
    }

    fun accessAny(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions? = null,
        onPermission: () -> Unit
    ): PermissionRequest {
        return checkAny(requestCode, *permissions, options = options) {
            if (it) onPermission()
        }
    }

    fun forceAccess(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions? = null,
        onPermission: () -> Unit
    ): PermissionRequest {
        var request: PermissionRequest? = null
        request = check(requestCode, *permissions, options = options) {
            if (it) onPermission() else request?.request()
        }
        return request
    }

    fun forceAccessAny(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions? = null,
        onPermission: () -> Unit
    ): PermissionRequest {
        var request: PermissionRequest? = null
        request = checkAny(requestCode, *permissions, options = options) {
            if (it) onPermission() else request?.request()
        }
        return request
    }

}

class PermissionSettingOptions(
    val titleDenied: String = "Permission denied",
    val messageDenied: String = "You need to allow permission to use this feature",
    val positive: String = "Ok"
)


abstract class PermissionDispatcher {
    abstract val isFinishing: Boolean
    abstract val activity: FragmentActivity
    private var mRechecked = hashMapOf<String, Int>()
    private var mOpenSettingDialog: AlertDialog? = null

    abstract fun requestPermission(permissions: Array<String>, requestCode: Int)

    abstract fun registryResultCallback(requestCode: Int, callback: () -> Unit)

    fun increaseRecheck(permissions: Array<out String>) {
        val key = permissions[0]
        mRechecked[key] = mRechecked[key].safe() + 1
    }

    fun clearRechecked(permissions: Array<out String>) {
        mRechecked.remove(permissions[0])
    }

    fun hasRecheck(permissions: Array<out String>): Boolean {
        return mRechecked[permissions[0]].safe() > 1
    }

    fun showSettingDialog(
        options: PermissionSettingOptions?,
        requestCode: Int,
        onCancel: () -> Unit
    ) {
        if (mOpenSettingDialog == null) {
            val settingOptions = options ?: PermissionSettingOptions()
            mOpenSettingDialog = AlertDialog.Builder(activity)
                .setTitle(settingOptions.titleDenied)
                .setMessage(settingOptions.messageDenied)
                .setPositiveButton(settingOptions.positive) { _: DialogInterface, _: Int ->
                    openSetting(requestCode)
                }
                .setOnCancelListener { onCancel() }
                .create()
        }
        mOpenSettingDialog!!.show()
    }

    private fun openSetting(requestCode: Int) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${activity.packageName}")
        )
        doOpenSetting(intent, requestCode)
    }

    abstract fun doOpenSetting(intent: Intent, requestCode: Int)
}

abstract class PermissionRequest(
    vararg permission: String,
    private val requestCode: Int,
    private val options: PermissionSettingOptions?,
    protected val dispatcher: PermissionDispatcher,
) : View.OnClickListener, () -> Unit {
    protected val permissions = permission as Array<String>

    init {
        dispatcher.registryResultCallback(requestCode) {
            notifyChange()
        }
    }

    abstract fun onResult(): Boolean

    fun request() {
        if (dispatcher.isFinishing) return
        if (permissions.isEmpty()) throw RuntimeException("No permission to check")
        doRequest()
    }

    protected abstract fun doRequest()

    override fun onClick(v: View?) {
        request()
    }

    override fun invoke() {
        request()
    }

    protected fun checkOrShowSetting() {
        if (shouldShowSettings(permissions)) {
            dispatcher.showSettingDialog(options, requestCode) { notifyChange() }
        } else {
            dispatcher.requestPermission(permissions, requestCode)
            dispatcher.increaseRecheck(permissions)
        }
    }

    private fun notifyChange() {
        if (onResult()) dispatcher.clearRechecked(permissions)
    }

    private fun shouldShowSettings(permissions: Array<out String>): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            dispatcher.activity,
            permissions.first()
        ) || dispatcher.hasRecheck(permissions)
    }
}