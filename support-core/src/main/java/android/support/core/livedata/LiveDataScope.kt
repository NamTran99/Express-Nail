package android.support.core.livedata

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

internal class LiveDataScope(private val delayTimeToClose: Long = 5000) {
    private var mScope: CoroutineScope? = null
    private val mLock = LiveDataScope::class
    private var mCancelJob: Job? = null

    val scope get() = mScope

    fun getOrCreate(): CoroutineScope {
        mCancelJob?.cancel()
        mCancelJob = null
        if (mScope == null) {
            synchronized(mLock) {
                if (mScope == null) mScope = object : CoroutineScope {
                    override val coroutineContext: CoroutineContext =
                        SupervisorJob() + Dispatchers.Main.immediate
                }
            }
        }
        return mScope!!
    }

    fun tryToClose(shouldCancelClose: () -> Boolean) {
        synchronized(mLock) {
            val scope = mScope ?: return
            mCancelJob = scope.launch {
                delay(delayTimeToClose)
                if (shouldCancelClose()) return@launch
                scope.cancel()
                mScope = null
                mCancelJob = null
            }
        }
    }
}