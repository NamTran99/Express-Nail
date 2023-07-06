package android.support.core.savedstate

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry

interface SavedStateCallback {
    fun onSavedState(): Bundle
    fun onRestoreState(savedState: Bundle)
}

fun SavedStateRegistry.registry(key: String, savedStateCallback: SavedStateCallback) {
    unregisterSavedStateProvider(key)
    registerSavedStateProvider(key) {
        savedStateCallback.onSavedState()
    }
    if (!this.isRestored) return
    val saved = consumeRestoredStateForKey(key) ?: return
    savedStateCallback.onRestoreState(saved)
}