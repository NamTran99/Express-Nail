package com.example.nailexpress.helper.network

import android.support.core.extensions.withIO
import com.example.nailexpress.exception.*
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import kotlin.reflect.KClass

open class AsyncAdapterFactory(
    protected val errorHandler: ApiCallErrorHandler
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        return object : CallAdapter<Any, Async<*>> {
            override fun responseType(): Type {
                if (returnType !is ParameterizedType)
                    error("Not support return type $returnType")
                val params = returnType.actualTypeArguments
                if (params.size != 1) error("Return generic parameters size should be 1")
                return responseTypeOf(params.first())
            }

            override fun adapt(call: Call<Any>): Async<*> {
                return convert(call, returnType, annotations)
            }

        }
    }

    protected open fun responseTypeOf(type: Type): Type {
        return type
    }

    protected open fun convert(
        call: Call<Any>,
        returnType: Type,
        annotations: Array<out Annotation>
    ): Async<*> {
        return AsyncImpl(call, errorHandler)
    }

    class ParameterizedTypeImpl(
        private val rawType: KClass<*>,
        private val parameters: Array<Type>
    ) : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> {
            return parameters
        }

        override fun getRawType(): Type {
            return rawType.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    open class AsyncImpl<R : Any>(
        private val call: Call<R>,
        private val errorHandler: ApiCallErrorHandler
    ) : Async<R> {
        override suspend fun awaitNullable(): R? {
            return withIO {
                val response = try {
                    call.execute()
                } catch (t: Throwable) {
                    throw errorHandler.onRequestError(t)
                }

                val body = response.body()
                if (!response.isSuccessful) {
                    throw errorHandler.onResponseError(response)
                }
                body
            }
        }

        override suspend fun clone(): Async<R> {
            return AsyncImpl(call.clone(), errorHandler)
        }
    }
}

class DefaultApiErrorHandler : ApiCallErrorHandler {
    override fun onRequestError(throwable: Throwable): Throwable {
        return if (throwable is ConnectException) {
            ConnectException("Can not connect to server, please try again.")
        } else throwable
    }

    override fun onResponseError(response: Response<out Any>): Throwable {
        val errorBody = response.errorBody()?.string()
            ?: response.message()
        val errorCode = response.code()

        return when {
            errorCode == 401 || errorCode == 403 -> UnauthorizedException()
            errorCode in 400..500 -> ApiRequestException(errorBody)
            errorCode > 500 -> InternalServerException()
            else -> ParameterInvalidException(response.message())
        }
    }
}

interface ApiCallErrorHandler {
    fun onRequestError(throwable: Throwable): Throwable

    fun onResponseError(response: Response<out Any>): Throwable
}

interface Async<R> {
    suspend fun awaitNullable(): R?
    suspend fun clone(): Async<R>
    suspend fun await(): R {
        return awaitNullable() ?: throw ServerResponseNullException()
    }
}