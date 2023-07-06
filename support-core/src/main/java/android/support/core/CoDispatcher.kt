package android.support.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoDispatcher {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher

    companion object : CoDispatcher {
        var delegate: CoDispatcher = DefaultDispatcher()
        override val io: CoroutineDispatcher get() = delegate.io

        override val main: CoroutineDispatcher get() = delegate.main
    }
}

private class DefaultDispatcher : CoDispatcher {
    override val io: CoroutineDispatcher get() = Dispatchers.IO
    override val main: CoroutineDispatcher get() = Dispatchers.Main
}