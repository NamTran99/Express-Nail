package android.support.core.savedstate

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSaveStateHandle
import androidx.savedstate.SavedStateRegistryOwner
import java.util.concurrent.atomic.AtomicReference

interface SavedStateHandler {
    fun <T> getLiveData(key: String): MutableLiveData<T>

    fun <T> getLiveData(key: String, initialValue: T): MutableLiveData<T>

    fun <T> getOr(key: String, def: () -> T): T

    fun <T> get(key: String): T?

    fun <T> set(key: String, value: T?)
}


class SavedStateHandlerFactory(private val owner: LifecycleOwner) {

    fun create(): SavedStateHandler {
        val savedStateRegistry = (owner as? SavedStateRegistryOwner)
            ?.savedStateRegistry
            ?: error("${owner.javaClass.name} should be SavedStateRegistryOwner")

        val lifecycle = owner.lifecycle
        val handle = createSaveStateHandle(
            savedStateRegistry,
            lifecycle,
            "android.support.core.saved.state.handler"
        )
        return SavedStateHandlerImpl(handle)
    }

    private class SavedStateHandlerImpl(private val handle: SavedStateHandle) : SavedStateHandler {

        override fun <T> getLiveData(key: String): MutableLiveData<T> {
            return handle.getLiveData(key)
        }

        override fun <T> getLiveData(key: String, initialValue: T): MutableLiveData<T> {
            return handle.getLiveData(key, initialValue)
        }

        override fun <T> getOr(key: String, def: () -> T): T {
            var result = handle.get<T>(key)
            if (result == null) {
                result = def()
                handle.set(key, result)
            }
            return result!!
        }

        override fun <T> get(key: String): T? {
            return handle.get<T>(key)
        }

        override fun <T> set(key: String, value: T?) {
            handle.set(key, value)
        }
    }
}
