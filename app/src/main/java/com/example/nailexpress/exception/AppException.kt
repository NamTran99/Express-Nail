package com.example.nailexpress.exception

import androidx.annotation.StringRes
import com.example.nailexpress.models.api.ApiError
import com.google.gson.Gson


class InternalServerException : RuntimeException("Error internal server")
class ServerResponseNullException : RuntimeException("Server response no Content")
class UnauthorizedException  : RuntimeException("Unauthorized")
class ParameterInvalidException(message: String) : RuntimeException(message)
interface IViewCustomerErrorHandler {
    fun handleError(@StringRes errorMessage: Int)
}

open class ApiRequestException(rawMessage: String?) : RuntimeException() {
    private val mMessage = rawMessage
        ?.let {
            try {
                Gson().fromJson(it, ApiError::class.java)
            } catch (e: Throwable) {
                ApiError(0, rawMessage, null, mutableListOf())
            }
        }
        ?.takeIf { it.body.isNotEmpty() }?.body ?: "Unknown"

    override val message: String
        get() = mMessage
}
