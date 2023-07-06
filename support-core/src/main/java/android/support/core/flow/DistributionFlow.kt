package android.support.core.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun <T> distributionFlowOf(value: T): DistributionFlow<T> = DistributionFlowImpl(value)
fun <T> distributionFlowOfList(): DistributionFlow<List<T>> = DistributionFlowImpl(emptyList())

fun <T> Flow<T>.distributeBy(flow: DistributionFlow<T>) {
    flow.connect(this)
}


interface DistributionFlow<T> : Flow<T> {
    fun connect(flow: Flow<T>)
    fun post(value: T)
    var value: T
}

private class DistributionFlowImpl<T>(def: T) : DistributionFlow<T>, Flow<T> {
    private var mVersion: Int = -1
    private var mSource: Source<T>? = null
    private var mLatestJob: Job? = null
    private val stateFlow = MutableStateFlow(def)
    private val channels = CollectorChannels<T>()

    override var value: T
        get() = stateFlow.value
        set(value) {
            stateFlow.value = value
        }
    private val hasNewSource: Boolean get() = mVersion != (mSource?.version ?: -1)

    override fun connect(flow: Flow<T>) {
        synchronized(this) {
            mLatestJob?.cancel()
            val source = Source(flow, (mSource?.version ?: -1) + 1)
            mSource = source
            channels.tryNotify()
        }
    }

    override fun post(value: T) {
        val lastValue = stateFlow.value
        if (lastValue == value) return
        stateFlow.value = value
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        // Each collector be collected, will start a coroutine to check source update
        try {
            val channel = channels.allocSlot(collector)
            startToUpdateSource(channel)
            stateFlow.collect(collector)
        } finally {
            channels.freeSlot(collector)
        }
    }

    private suspend fun startToUpdateSource(channel: CollectorChannel<T>) {
        CoroutineScope(currentCoroutineContext()).launch {
            while (true) {
                ensureActive()
                // Double check to reduce lock time
                // First check to lock update source
                if (hasNewSource) {
                    synchronized(this@DistributionFlowImpl) {
                        // Assume has 2 coroutine receive new source at the same time
                        // c1 will lock first, and c2 will be waiting for c1 completed.
                        // c1 update successfully, c2 will work next and check, if source updated then it will do no thing
                        if (hasNewSource) {
                            val source = mSource!!
                            mVersion = source.version
                            mLatestJob = launch {
                                stateFlow.emitAll(source.flow)
                            }
                        }
                    }
                }
                channel.await()
            }
        }
    }

    class Source<T>(val flow: Flow<T>, val version: Int)
}