package android.support.core.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

interface OpenForResultAction {
    fun launch(intent: Intent)
    fun launch(args: Bundle)
    fun launch(args: BundleArgument) {
        launch(args.toBundle())
    }
}

open class OpenForResultActionImpl(
    private val register: ActivityResultRegister,
    private val clazz: KClass<out Activity>,
    private val callback: (ActivityResult) -> Unit
) : OpenForResultAction {
    private var launcher: ActivityResultLauncher<Intent>

    init {
        val contract = ActivityResultContracts.StartActivityForResult()
        launcher = register.registerForActivityResult(contract) {
            callback(it)
        }
    }

    override fun launch(args: Bundle) {
        val context = if (register is Fragment) register.requireContext()
        else (register as Activity)
        launcher.launch(Intent(context, clazz.java).apply {
            putExtras(args)
        })
    }

    override fun launch(intent: Intent) {
        launcher.launch(intent)
    }
}

class OpenForResultOkAction(
    register: ActivityResultRegister,
    clazz: KClass<out Activity>,
    callback: (Intent?) -> Unit
) : OpenForResultActionImpl(register, clazz, {
    if (it.resultCode == Activity.RESULT_OK) {
        callback(it.data)
    }
})