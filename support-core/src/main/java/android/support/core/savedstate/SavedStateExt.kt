//Do not change this package
//androidx.lifecycle
package androidx.lifecycle

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry

internal val SavedStateHandle.saveStateProvider get() = savedStateProvider()

internal fun createSaveStateHandle(
    registry: SavedStateRegistry, lifecycle: Lifecycle, key: String,
): SavedStateHandle {
    return LegacySavedStateHandleController.create(registry, lifecycle, key, Bundle()).handle
}