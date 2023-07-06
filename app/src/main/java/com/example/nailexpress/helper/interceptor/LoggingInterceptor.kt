package com.example.nailexpress.helper.interceptor

import android.text.TextUtils
import com.example.nailexpress.extension.safe
import com.example.nailexpress.helper.interceptor.Printer.Companion.getJsonString
import com.example.nailexpress.helper.interceptor.Printer.Companion.printFileRequest
import com.example.nailexpress.helper.interceptor.Printer.Companion.printJsonRequest
import com.example.nailexpress.helper.interceptor.Printer.Companion.printJsonResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class LoggingInterceptor(private val builder: Builder) : Interceptor {

    private val debug: Boolean = builder.isDebug()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val headerMap: HashMap<String, String> = builder.getHeaders()
        var rSubtype: String?
        var key: String?
        if (headerMap.size > 0) {
            val requestBuilder: Request.Builder = request.newBuilder()
            for (o in headerMap.keys) {
                rSubtype = o
                key = headerMap[rSubtype]
                assert(key != null)
                requestBuilder.addHeader(rSubtype, key!!)
            }
            request = requestBuilder.build()
        }

        val queryMap: HashMap<String, String> = builder.getHttpUrl()
        if (queryMap.size > 0) {
            val httpUrlBuilder: HttpUrl.Builder? = request.url.newBuilder(request.url.toString())
            for (o in queryMap.keys) {
                key = o
                val value = queryMap[key]
                assert(httpUrlBuilder != null)
                httpUrlBuilder?.addQueryParameter(key, value)
            }
            request = request.newBuilder().url(httpUrlBuilder!!.build()).build()
        }

        return if (this.debug && builder.getLevel() !== Logger.Level.NONE) {
            val requestBody = request.body
            rSubtype = null
            if (requestBody?.contentType() != null) {
                rSubtype = requestBody.contentType()!!.subtype
            }
            val executor = builder.getExecutor()
            if (this.isNotFileRequest(rSubtype)) {
                if (executor != null) {
                    executor.execute(createPrintJsonRequestRunnable(builder, request))
                } else {
                    Printer.printJsonRequest(builder, request)
                }
            } else if (executor != null) {
                executor.execute(createFileRequestRunnable(builder, request))
            } else {
                Printer.printFileRequest(builder, request)
            }
            val st = System.nanoTime()
            val response: Response = if (builder.isEnableMock()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(builder.getSleepMs())
                } catch (var24: InterruptedException) {
                    var24.printStackTrace()
                }
                Response.Builder()
                    .body(
                        builder.getListener()
                            ?.getJsonResponse(request)
                            .safe()
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .request(chain.request()).protocol(Protocol.HTTP_2)
                    .message("Mock")
                    .code(200)
                    .build()
            } else {
                chain.proceed(request)
            }
            val chainMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - st)
            val segmentList = request.url.encodedPathSegments
            val header = response.headers.toString()
            val code = response.code
            val isSuccessful = response.isSuccessful
            val message = response.message
            val responseBody = response.body
            val contentType = responseBody?.contentType() ?: "application/json".toMediaTypeOrNull()
            var subtype: String? = null
            if (contentType != null) {
                subtype = contentType.subtype
            }
            if (this.isNotFileRequest(subtype)) {
                val bodyString: String = Printer.getJsonString(responseBody?.string().safe()).safe()
                val url = response.request.url.toString()
                if (executor != null) {
                    executor.execute(
                        createPrintJsonResponseRunnable(
                            builder,
                            chainMs,
                            isSuccessful,
                            code,
                            header,
                            bodyString,
                            segmentList,
                            message,
                            url
                        )
                    )
                } else {
                    Printer.printJsonResponse(
                        builder,
                        chainMs,
                        isSuccessful,
                        code,
                        header,
                        bodyString,
                        segmentList,
                        message,
                        url
                    )
                }
                val body = bodyString.toResponseBody(contentType)
                response.newBuilder().body(body).build()
            } else {
                if (executor != null) {
                    executor.execute(
                        createFileResponseRunnable(
                            builder,
                            chainMs,
                            isSuccessful,
                            code,
                            header,
                            segmentList,
                            message
                        )
                    )
                } else {
                    Printer.printFileResponse(
                        builder,
                        chainMs,
                        isSuccessful,
                        code,
                        header,
                        segmentList,
                        message
                    )
                }
                response
            }
        } else {
            chain.proceed(request)
        }
    }

    private fun isNotFileRequest(subtype: String?): Boolean {
        return subtype != null
                && (subtype.contains("json")
                || subtype.contains("xml")
                || subtype.contains("plain")
                || subtype.contains("html"))
    }

    private fun createPrintJsonRequestRunnable(
        builder: Builder,
        request: Request
    ): Runnable {
        return Runnable { Printer.printJsonRequest(builder, request) }
    }

    private fun createFileRequestRunnable(
        builder: Builder,
        request: Request
    ): Runnable {
        return Runnable { Printer.printFileRequest(builder, request) }
    }

    private fun createPrintJsonResponseRunnable(
        builder: Builder,
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        bodyString: String,
        segments: List<String>,
        message: String,
        responseUrl: String
    ): Runnable {
        return Runnable {
            Printer.printJsonResponse(
                builder,
                chainMs,
                isSuccessful,
                code,
                headers,
                bodyString,
                segments,
                message,
                responseUrl
            )
        }
    }

    private fun createFileResponseRunnable(
        builder: Builder,
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        segments: List<String>,
        message: String
    ): Runnable {
        return Runnable {
            Printer.printFileResponse(
                builder,
                chainMs,
                isSuccessful,
                code,
                headers,
                segments,
                message
            )
        }
    }

    class Builder {
        private var tag = "ILT-SDK"
        private val headers: HashMap<String, String>
        private val httpUrl: HashMap<String, String>
        private var logHackEnable = false
        private var debug = false
        private var type = 4
        private var requestTag: String? = null
        private var responseTag: String? = null
        private var level: Logger.Level
        private var logger: Logger = Logger.DEFAULT
        private var executor: Executor? = null
        private var mockEnabled = false
        private var sleepMs: Long = 0
        private var listener: BufferListener? = null

        fun isLogHackEnable(): Boolean = logHackEnable

        fun isDebug(): Boolean = debug

        fun getType() = type

        fun getLevel(): Logger.Level = level

        fun setLevel(level: Logger.Level): Builder = apply { this.level = level }

        fun getHeaders(): HashMap<String, String> = headers

        fun getHttpUrl(): HashMap<String, String> = httpUrl

        fun getTag(isRequest: Boolean): String {
            return if (isRequest) {
                if (TextUtils.isEmpty(requestTag)) tag else requestTag!!
            } else {
                if (TextUtils.isEmpty(responseTag)) tag else responseTag!!
            }
        }

        fun getLogger(): Logger = logger

        fun addHeader(name: String, value: String): Builder = apply { headers[name] = value }

        fun addQueryParam(name: String, value: String): Builder = apply { httpUrl[name] = value }

        fun tag(tag: String): Builder = apply { this.tag = tag }

        fun request(tag: String?): Builder = apply { requestTag = tag }

        fun response(tag: String?): Builder = apply { responseTag = tag }

        fun loggable(isDebug: Boolean): Builder = apply { this.debug = isDebug }

        fun log(type: Int): Builder = apply { this.type = type }

        fun logger(logger: Logger): Builder = apply { this.logger = logger }

        fun executor(executor: Executor?): Builder = apply { this.executor = executor }

        fun getExecutor(): Executor? = executor

        fun getListener() = listener

        fun enableMock(useMock: Boolean, sleep: Long, listener: BufferListener?): Builder = apply {
            mockEnabled = useMock
            sleepMs = sleep
            this.listener = listener
        }

        fun isEnableMock() = mockEnabled

        fun getSleepMs() = sleepMs

        fun enableAndroidStudioV3LogsHack(useHack: Boolean): Builder =
            apply { logHackEnable = useHack }

        fun build(): LoggingInterceptor = LoggingInterceptor(this)

        init {
            level = Logger.Level.BASIC
            headers = HashMap()
            httpUrl = HashMap()
        }
    }
}