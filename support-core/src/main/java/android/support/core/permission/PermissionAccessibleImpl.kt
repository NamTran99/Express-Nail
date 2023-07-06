package android.support.core.permission

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionAccessibleImpl : PermissionAccessible {

    private var mDispatcher: PermissionDispatcher? = null
    private val dispatcher get() = mDispatcher ?: error("Dispatcher should not be null")

    fun setDispatcher(dispatcher: PermissionDispatcher) {
        mDispatcher = dispatcher
    }

    override fun check(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions?,
        onPermission: (Boolean) -> Unit
    ): PermissionRequest {
        return PermissionRequestImpl(
            *permissions,
            requestCode = requestCode,
            options = options,
            dispatcher = dispatcher,
            onPermission = { onPermission(it) }
        )
    }

    override fun checkAny(
        requestCode: Int,
        vararg permissions: String,
        options: PermissionSettingOptions?,
        onPermission: (Boolean) -> Unit
    ): PermissionRequest {
        return PermissionRequestAnyImpl(
            *permissions,
            requestCode = requestCode,
            options = options,
            dispatcher = dispatcher,
            onPermission = { onPermission(it) }
        )
    }
}

class PermissionRequestImpl(
    vararg permission: String,
    requestCode: Int,
    options: PermissionSettingOptions?,
    dispatcher: PermissionDispatcher,
    private val onPermission: PermissionRequest.(Boolean) -> Unit
) : PermissionRequest(
    *permission,
    requestCode = requestCode,
    options = options,
    dispatcher = dispatcher,
) {
    override fun onResult(): Boolean {

        val isAllAllowed = permissions.all {
            ContextCompat.checkSelfPermission(
                dispatcher.activity, it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllAllowed) {
            onPermission(true)
            return true
        }
        onPermission(false)
        return false
    }

    override fun doRequest() {
        checkOrShowSetting()
    }
}

class PermissionRequestAnyImpl(
    vararg permission: String,
    requestCode: Int,
    options: PermissionSettingOptions?,
    dispatcher: PermissionDispatcher,
    private val onPermission: PermissionRequest.(Boolean) -> Unit
) : PermissionRequest(
    *permission,
    requestCode = requestCode,
    options = options,
    dispatcher = dispatcher,
) {

    private fun isAnyAllowed(): Boolean {
        return permissions.any {
            ContextCompat.checkSelfPermission(
                dispatcher.activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onResult(): Boolean {
        val isAnyAllowed = permissions.any {
            ContextCompat.checkSelfPermission(
                dispatcher.activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAnyAllowed) {
            onPermission(true)
            return true
        }
        onPermission(false)
        return false
    }

    override fun doRequest() {
        if (isAnyAllowed()) {
            if (dispatcher.hasRecheck(permissions)) {
                onPermission(true)
                return
            }

            if (permissions.any {
                    !ActivityCompat.shouldShowRequestPermissionRationale(dispatcher.activity, it)
                }) {
                onPermission(true)
                return
            }

        }

        checkOrShowSetting()
    }
}