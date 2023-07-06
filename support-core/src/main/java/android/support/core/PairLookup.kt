package android.support.core

class PairLookup<A, B>(pairs: Array<out Pair<A, B>>) {
    private val value2Key = hashMapOf<B, A>()
    private val key2Value = pairs.toMap().onEach {
        value2Key[it.value] = it.key
    }

    fun keyOf(value: B): A? {
        return value2Key[value]
    }

    fun requireKeyOf(value: B): A {
        return value2Key[value] ?: error("Not found key of ${value.toString()}")
    }

    fun valueOf(key: A): B? {
        return key2Value[key]
    }

    fun requireValueOf(key: A): B {
        return key2Value[key] ?: error("Not found value of ${key.toString()}")
    }
}

fun <A, B : Any> pairLookupOf(
    vararg pairs: Pair<A, B>,
): PairLookup<A, B> {
    return PairLookup(pairs)
}