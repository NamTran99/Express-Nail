package com.example.nailexpress.views.widgets.topbar

import android.os.Bundle
import android.support.core.view.ViewSavedStateRegistry
import android.support.core.view.ViewState
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.savedstate.SavedStateRegistryOwner

interface TopBarOwner {
		val topBar: TopBarAdapter
				get() {
						if (this !is Fragment)
								error("Top bar default method just support fragment, please implement this function in activity")
						return (activity as? TopBarOwner)?.topBar ?: error("Not found top bar implementation")
				}
}

interface TopBarAdapter {
    fun setState(state: TopBarState)
    fun <T : TopBarState> state(): T
    fun removeView()
}

sealed class TopBarState : ViewState()
class NoTopBarState : TopBarState()

class TopBarAdapterImpl(
		private val owner: SavedStateRegistryOwner,
		private val viewGroup: ViewGroup
) : TopBarAdapter {
    private var mState: TopBarState = NoTopBarState()
    private val inflater = LayoutInflater.from(viewGroup.context)
    private val registry = ViewSavedStateRegistry(owner, "android.support.topbar") { mState }
    override fun setState(state: TopBarState) {
        mState = state.applyTo(viewGroup, mState, inflater, registry) as TopBarState
    }

    override fun <T : TopBarState> state(): T {
        return mState as T
    }

    override fun removeView() {
        viewGroup.removeAllViews()
    }
}

