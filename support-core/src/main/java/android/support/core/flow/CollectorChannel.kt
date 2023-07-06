package android.support.core.flow

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector

class CollectorChannel<T>(val collector: FlowCollector<T>) {
    private val channel = Channel<Unit>(1)
    suspend fun await() {
        channel.receive()
    }

    fun tryNotify() {
        channel.trySend(Unit)
    }
}

class CollectorChannels<T> {
    private val channels = ArrayList<CollectorChannel<T>>()

    fun allocSlot(collector: FlowCollector<T>): CollectorChannel<T> {
        val collectorChannel = CollectorChannel(collector)
        synchronized(this) {
            channels.add(collectorChannel)
        }
        return collectorChannel
    }

    fun freeSlot(collector: FlowCollector<T>) {
        synchronized(channels) {
            channels.find { it.collector == collector }?.also {
                channels.remove(it)
            }
        }
    }

    fun tryNotify() = synchronized(channels) {
        channels.forEach { it.tryNotify() }
    }

    fun freeSlot(channel: CollectorChannel<T>) {
        synchronized(channels) {
            channels.remove(channel)
        }
    }
}