package android.support.core.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

fun <T> pipeFlowOf(): PipeFlow<T> = PipeFlowImpl()

interface PipeFlow<T> : Flow<T> {
    fun connect(flow: Flow<T>)
}

suspend fun <T> Flow<T>.transportBy(flow: PipeFlow<T>) {
    flow.connect(this)
}

private class PipeFlowImpl<T> : PipeFlow<T> {
    private var mSource: Source<T>? = null
    private val channels = CollectorChannels<T>()

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun collect(collector: FlowCollector<T>) {
        try {
            val channel = channels.allocSlot(collector)
            val currentContext = currentCoroutineContext()[Job]
            while (true) {
                currentContext!!.ensureActive()

                val source = mSource

                // Double check to reduce lock time
                if (source != null && !source.isCollected(collector)) {
                    synchronized(this@PipeFlowImpl) {
                        if (!source.isCollected(collector)) {
                            source.collect(collector, currentContext)
                        }
                    }
                }
                channel.await()
            }
        } finally {
            channels.freeSlot(collector)
        }
    }

    override fun connect(flow: Flow<T>) {
        synchronized(this) {
            mSource?.cancel()
            mSource = Source(flow)
            channels.tryNotify()
        }
    }

    class Source<T>(val flow: Flow<T>) {
        private val mCollected = hashMapOf<FlowCollector<T>, Boolean>()
        private var mJob: Job? = null

        @InternalCoroutinesApi
        fun collect(collector: FlowCollector<T>, context: CoroutineContext) {
            synchronized(mCollected) { mCollected[collector] = true }
            mJob = CoroutineScope(context).launch {
                flow.collect(collector)
            }
        }

        fun cancel() {
            mJob?.cancel()
        }

        fun isCollected(collector: FlowCollector<T>): Boolean {
            return synchronized(mCollected) { mCollected[collector] ?: false }
        }
    }

}