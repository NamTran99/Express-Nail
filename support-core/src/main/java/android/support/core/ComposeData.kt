package android.support.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext

interface ComposeData {
    operator fun <T> component1(): T
    operator fun <T> component2(): T
    operator fun <T> component3(): T
}

abstract class AbstractComposeData(size: Int) : ComposeData {
    companion object {
        val EMPTY = Any()
    }

    protected val array = Array<Any?>(size) { EMPTY }

    @Suppress("unchecked_cast")
    protected fun <T> componentOf(i: Int): T {
        if (array.size <= i) error("Component $i out of array size")
        return array[i] as T
    }

    override operator fun <T> component1(): T = componentOf(0)
    override operator fun <T> component2(): T = componentOf(1)
    override operator fun <T> component3(): T = componentOf(2)

    protected fun add(index: Int, it: Any?) {
        array[index] = it
    }

    protected fun isAllAdded(): Boolean {
        return array.all { it != EMPTY }
    }

    protected fun isAnyActivated(): Boolean {
        return array.any { it != EMPTY }
    }
}

class ComposeDataImpl(size: Int) : AbstractComposeData(size) {

    private var awaitAll: (ComposeData) -> Unit = {}
    private var anyActivated: (ComposeData) -> Unit = {}

    fun awaitAll(callback: (ComposeData) -> Unit) {
        awaitAll = callback
    }

    fun anyActivated(callback: (ComposeData) -> Unit) {
        anyActivated = callback
    }

    fun put(index: Int, it: Any?) {
        super.add(index, it)
        if (isAllAdded()) awaitAll(this)
        if (isAnyActivated()) anyActivated(this)
    }
}

class SuspendComposeDataImpl(size: Int) : AbstractComposeData(size) {
    private var onAllSuccess: suspend CoroutineScope.(ComposeData) -> Unit = {}
    private var anyActivated: suspend CoroutineScope.(ComposeData) -> Unit = {}

    fun awaitAll(callback: suspend CoroutineScope.(ComposeData) -> Unit) {
        onAllSuccess = callback
    }

    fun anyActivated(callback: suspend CoroutineScope. (ComposeData) -> Unit) {
        anyActivated = callback
    }

    suspend fun put(index: Int, it: Any?) {
        super.add(index, it)
        with(CoroutineScope(currentCoroutineContext())) {
            if (isAllAdded()) onAllSuccess(this, this@SuspendComposeDataImpl)
            if (isAnyActivated()) anyActivated(this, this@SuspendComposeDataImpl)
        }
    }
}