package android.support.core.view

import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import androidx.viewbinding.ViewBinding

abstract class ViewState(private val removeContent: ViewGroup.() -> Unit = { removeAllViews() }) {
    private lateinit var mInflater: LayoutInflater
    private lateinit var mViewGroup: ViewGroup
    private var mReuseBinding: ViewBinding? = null

    protected open val stateBinding: ViewBinding = EmptyViewBinding()
    protected val parent get() = mViewGroup

    @Suppress("unchecked_cast")
    protected fun <T> bindingOf(factory: (LayoutInflater, ViewGroup, Boolean) -> T): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE) {
            if (!::mViewGroup.isInitialized) error("Not call fun apply to yet")
            if (mReuseBinding != null) mReuseBinding!! as T
            else factory(mInflater, mViewGroup, false)
        }

    fun saveState(outState: Bundle) {
        if (stateBinding !is EmptyViewBinding && ::mViewGroup.isInitialized) {
            onSaveState(outState)
        }
    }

    protected open fun onSaveState(outState: Bundle) {}

    fun applyTo(
        viewGroup: ViewGroup,
        previous: ViewState? = null,
        inflater: LayoutInflater = LayoutInflater.from(viewGroup.context),
        savedConsumer: SavedStateConsumer? = null
    ): ViewState {
        mViewGroup = viewGroup
        mInflater = inflater
        if (previous != null && previous.javaClass == javaClass) {
            mReuseBinding = previous.stateBinding
            doApply(savedConsumer?.consume())
            return this
        }
        removeContent(viewGroup)
        if (stateBinding is EmptyViewBinding) {
            (stateBinding as EmptyViewBinding).root = viewGroup
            return this
        }
        viewGroup.addView(stateBinding.root)
        doApply(savedConsumer?.consume())
        return this
    }

    protected open fun doApply(savedState: Bundle?) {
        doApply()
    }

    protected open fun doApply() {}

    private class EmptyViewBinding : ViewBinding {
        private var mRoot: View? = null

        override fun getRoot(): View {
            return mRoot ?: error("Not set yet")
        }

        fun setRoot(view: View) {
            mRoot = view
        }
    }
}

interface SavedStateConsumer {
    fun consume(): Bundle?
}

class ViewSavedStateRegistry(
    private val owner: SavedStateRegistryOwner,
    private val key: String = "android.support.view.state",
    private val viewStateGetter: () -> ViewState
) : SavedStateConsumer {
    init {
        owner.savedStateRegistry.registerSavedStateProvider(key) {
            Bundle().also {
                viewStateGetter.invoke().saveState(it)
            }
        }
    }

    override fun consume(): Bundle? {
        return owner.savedStateRegistry.consumeRestoredStateForKey(key)
    }
}

