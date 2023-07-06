package android.support.core.flow


fun <T> stateFlowOf(value: T): SupportStateFlow<T> {
    return SupportStateFlowImpl(value)
}

fun <T> stateFlowOfList(value: List<T> = emptyList()): SupportStateFlow<List<T>> {
    return SupportStateFlowImpl(value)
}

fun stateFlowOf(value: String = ""): SupportStateFlow<String> {
    return SupportStateFlowImpl(value)
}

