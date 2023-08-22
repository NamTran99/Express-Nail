package com.example.nailexpress.helper.interceptor

import com.example.nailexpress.datasource.local.SharePrefKey
import com.example.nailexpress.datasource.local.SharePrefs
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NoTokenRequired

class TokenInterceptor(private val userLocalSource: SharePrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val isNoTokenRequired = originRequest.tag(Invocation::class.java)?.method()
            ?.getAnnotation(NoTokenRequired::class.java) != null
        if (isNoTokenRequired) return chain.proceed(originRequest)

        var request = originRequest.newBuilder().addHeader("Content-Language", "vi").build()
        val token = userLocalSource.get<String>(SharePrefKey.TOKEN)
        if (!token.isNullOrEmpty()) {
            val bearer = "Bearer $token"
            request = request.newBuilder()
                .addHeader("Authorization", bearer)
                .build()
        }
        return chain.proceed(request)
    }
}
