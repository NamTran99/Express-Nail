package android.support.core.lifecycle

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

class LifecycleOwnerDelegate {
    private var mOwner: LifecycleOwner? = null

    private val mObserver = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
                mOwner = null
            }
        }
    }

    fun get(): LifecycleOwner {
        return mOwner
            ?: error("Not set lifecycle owner yet or has been released")
    }

    private fun set(owner: SavedStateRegistryOwner) {
        if (mOwner == owner) return
        mOwner?.lifecycle?.removeObserver(mObserver)
        mOwner = owner
        owner.lifecycle.addObserver(mObserver)
    }

    class Store : ViewModel() {
        var delegate: LifecycleOwnerDelegate? = null

        override fun onCleared() {
            delegate = null
            super.onCleared()
        }
    }

    class OnRecreation : SavedStateRegistry.AutoRecreated {
        override fun onRecreated(owner: SavedStateRegistryOwner) {
            owner as ViewModelStoreOwner
            val store = ViewModelProvider(owner)[Store::class.java]
            store.delegate?.set(owner)
            owner.savedStateRegistry.runOnNextRecreation(OnRecreation::class.java)
        }
    }

    companion object {

        fun of(owner: LifecycleOwner): LifecycleOwnerDelegate {
            owner as ViewModelStoreOwner
            val store = ViewModelProvider(owner)[Store::class.java]
            if (store.delegate != null) {
                return store.delegate!!
            }
            val registry = (owner as SavedStateRegistryOwner).savedStateRegistry
            val delegate = LifecycleOwnerDelegate()
            tryToAddReCreator(registry, owner.lifecycle)
            delegate.set(owner)
            store.delegate = delegate
            return delegate
        }

        private fun tryToAddReCreator(registry: SavedStateRegistry, lifecycle: Lifecycle) {
            val currentState = lifecycle.currentState
            if (currentState == Lifecycle.State.INITIALIZED || currentState.isAtLeast(Lifecycle.State.STARTED)) {
                registry.runOnNextRecreation(OnRecreation::class.java)
                return
            }
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event,
                ) {
                    if (event == Lifecycle.Event.ON_START) {
                        lifecycle.removeObserver(this)
                        registry.runOnNextRecreation(OnRecreation::class.java)
                    }
                }
            })
        }
    }
}