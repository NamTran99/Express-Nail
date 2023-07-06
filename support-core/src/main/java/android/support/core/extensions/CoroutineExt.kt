package android.support.core.extensions

import android.support.core.CoDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext


suspend fun <T> withIO(function: suspend CoroutineScope.() -> T) =
    withContext(CoDispatcher.io) { function() }

suspend fun <T> withMain(function: suspend CoroutineScope.() -> T) =
    withContext(CoDispatcher.main) { function() }