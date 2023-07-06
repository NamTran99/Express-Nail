package android.support.core.permission

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class AbstractPermissionDispatcher : PermissionDispatcher() {
    private val mPermissions = SparseArray<ActivityResultLauncher<Array<String>>>()
    private val mSettings = SparseArray<ActivityResultLauncher<Intent>>()
    override val isFinishing: Boolean
        get() = activity.isFinishing

    abstract fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I>?

    final override fun requestPermission(permissions: Array<String>, requestCode: Int) {
        mPermissions[requestCode]?.launch(permissions)
    }

    final override fun registryResultCallback(requestCode: Int, callback: () -> Unit) {
        val contract = ActivityResultContracts.RequestMultiplePermissions()
        val permissionLauncher = registerForActivityResult(contract) {
            callback()
        }
        mPermissions.put(requestCode, permissionLauncher)

        val settingContract = OpenSettingContract()
        val settingLauncher = registerForActivityResult(settingContract) {
            callback()
        }
        mSettings.put(requestCode, settingLauncher)
    }

    final override fun doOpenSetting(intent: Intent, requestCode: Int) {
        mSettings[requestCode]?.launch(intent)
    }

    private class OpenSettingContract :
        ActivityResultContract<Intent, Unit>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?) {
        }
    }

}

class ActivityDispatcher(
    override val activity: FragmentActivity
) : AbstractPermissionDispatcher() {
    override val isFinishing: Boolean
        get() = activity.isFinishing

    override fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return activity.registerForActivityResult(contract, callback)
    }
}

class FragmentDispatcher(val fragment: Fragment) : AbstractPermissionDispatcher() {
    override val isFinishing: Boolean
        get() = activity.isFinishing || !fragment.isAdded || fragment.isDetached

    override val activity: FragmentActivity
        get() = fragment.requireActivity()

    override fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I>? {
        return fragment.registerForActivityResult(contract, callback)
    }
}