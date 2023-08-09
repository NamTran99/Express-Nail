package com.example.nailexpress.di

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.nailexpress.app.AppConfig
import com.example.nailexpress.helper.factory.TLSSocketFactory
import com.example.nailexpress.helper.interceptor.Logger
import com.example.nailexpress.helper.interceptor.LoggingInterceptor
import com.example.nailexpress.helper.interceptor.TokenInterceptor
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.helper.interceptor.Logging
import com.example.nailexpress.helper.network.ApiAsyncAdapterFactory
import com.example.nailexpress.helper.network.DefaultApiErrorHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    /**
     *@sample providesHttpLoggingInterceptor() by retrofit provider, can register with OkHttp, with this object we can
     * printf all activities of  as:
     *  request/response/header/body/..
     */
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * @sample providesLoggingInterceptorBuilder using addHeader / queryParam for request / response from server
     */
    @Singleton
    @Provides
    fun providesLoggingInterceptorBuilder(): LoggingInterceptor = LoggingInterceptor.Builder()
        .loggable(BuildConfig.DEBUG)
        .setLevel(Logger.Level.BASIC)
        .log(Platform.INFO)
        .request("Request")
        .response("Response")
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept", "application/json")
        .build()


    @Provides
    @Singleton
    fun provideTokenInterceptor(userLocalSource: SharePrefs): Interceptor =
        TokenInterceptor(userLocalSource)


    /**
     * @sample provideGson
     * init gson using for ConverterFactory retrofit convert json -> object
     */
    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    /**
     * @param cache If the same network resource needs to be accessed again after accessing it recently,
     * the device will not need to make a request to the server; it will response from cached
     * @sample providesOkHttpClient provider OkHttp allow using Http protocol easily
     */
    @Singleton
    @Provides
    fun providesOkHttpClient(
        application: Application,
        tokenInterceptor: Interceptor,
    ): OkHttpClient {
        val cacheDir = File(application.cacheDir, this.javaClass.simpleName)
        val cache = Cache(cacheDir, 10485760L) // 10mb
        val tlsSocketFactory = TLSSocketFactory()
        val interceptor = HttpLoggingInterceptor()
        val logging = Logging().setDebug(BuildConfig.DEBUG).setTag("RETROFIT_LOG")

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.unsafeTrustManager())
            .hostnameVerifier { _, _ -> true }
            .cache(cache)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(interceptor)
            .addInterceptor(ChuckerInterceptor(application))
            .addInterceptor(logging)
            .connectTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build()
    }


    /**
     * @sample provideRetrofit return retrofit.builder
     */
    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val tlsSocketFactory = TLSSocketFactory()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                httpClient
            )
            .baseUrl(AppConfig.endpoint)
            .addCallAdapterFactory(ApiAsyncAdapterFactory(DefaultApiErrorHandler()))
            .build()
    }


}