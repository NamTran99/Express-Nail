package android.support.core.extensions

fun Boolean?.safe(def: Boolean = false): Boolean {
    return this ?: def
}

fun Int?.safe(def: Int = 0): Int {
    return this ?: def
}

infix fun Boolean.throws(message: String) {
    if (this) throw IllegalArgumentException(message)
}

inline fun <T : Any> block(any: T?, block: T.() -> Unit) {
    if (any != null) block(any)
}

operator fun <T> T.plus(items: List<T>): List<T> {
    if (items is ArrayList) return items.also { it.add(0, this) }
    return items.toMutableList().also { it.add(0, this) }
}

fun <E, K> List<E>?.toMap(keyOf: E.() -> K): HashMap<K, E> {
    val hashMap = hashMapOf<K, E>()
    if (this == null) return hashMap
    forEach {
        hashMap[keyOf(it)] = it
    }
    return hashMap
}

interface TryBlock<T> {

    infix fun returns(block: (Throwable) -> T): T
    infix fun answers(block: (Throwable) -> Unit)
    infix fun throws(block: (Throwable) -> Throwable): T

    suspend infix fun suspendReturns(block: suspend (Throwable) -> T): T
    suspend infix fun suspendAnswers(block: suspend (Throwable) -> Unit)
    suspend infix fun suspendThrows(block: suspend (Throwable) -> Throwable): T

    class Success<T>(private val result: T) : TryBlock<T> {
        override fun returns(block: (Throwable) -> T): T {
            return result
        }

        override fun answers(block: (Throwable) -> Unit) {
        }

        override fun throws(block: (Throwable) -> Throwable): T {
            return result
        }

        override suspend fun suspendReturns(block: suspend (Throwable) -> T): T {
            return result
        }

        override suspend fun suspendAnswers(block: suspend (Throwable) -> Unit) {
        }

        override suspend fun suspendThrows(block: suspend (Throwable) -> Throwable): T {
            return result
        }
    }

    class Error<T>(private val e: Throwable) : TryBlock<T> {
        override fun returns(block: (Throwable) -> T): T {
            return block(e)
        }

        override fun answers(block: (Throwable) -> Unit) {
            block(e)
        }

        override fun throws(block: (Throwable) -> Throwable): T {
            throw block(e)
        }

        override suspend fun suspendReturns(block: suspend (Throwable) -> T): T {
            return block(e)
        }

        override suspend fun suspendAnswers(block: suspend (Throwable) -> Unit) {
            block(e)
        }

        override suspend fun suspendThrows(block: suspend (Throwable) -> Throwable): T {
            throw block(e)
        }
    }
}

inline fun <T> tryWith(function: () -> T): TryBlock<T> {
    return try {
        TryBlock.Success(function())
    } catch (e: Throwable) {
        TryBlock.Error(e)
    }
}