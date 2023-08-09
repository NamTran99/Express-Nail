package com.example.nailexpress.helper.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.charset
import okhttp3.logging.internal.isProbablyUtf8
import okio.Buffer
import okio.GzipSource
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.Charset

class Logging : Interceptor {
    private var debug = true
    private var TAG = "OKHTTP"
    fun setDebug(isDebug: Boolean = true) = apply {
        debug = true
    }

    fun setTag(tag: String) = apply {
        this.TAG = tag
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val response: Response
        try {
            response = chain.proceed(request)
            logRequest(debug, TAG, request, response)
        } catch (e: Exception) {
            logBuilder.append("HTTP FAILD:= ${e.message}\n")
            Log.d(TAG, "intercept: exception: ${logBuilder.toString()}")
            logBuilder.clear()
            throw e
        }
        return response
    }

    companion object {
        val logBuilder = StringBuilder()
        fun logRequest(debug: Boolean, tag: String, request: Request, response: Response) {
            if (debug) {
                logBuilder.clear()
                logBuilder.append("-----------------------------------------OKHTTP LOG-------------------------\n")
                logBuilder.append("-----------------------------------------HEADER------------------\n")
                request.headers.forEach {
                    logBuilder.append("${it.first} |----| ${it.second}\n")
                }

                logBuilder.append("-----------------------------------------BODY-----------------------\n")
                logBuilder.append("Method: ${request.method} URL: ${request.url}")

                var buffer = Buffer()
                request.body?.writeTo(buffer)

                val charset: Charset? = request.body?.contentType()?.charset()

                logBuilder.append("\n")

                logBuilder.append("-----------------------------------------RESPONSE------------------------\n")
                logBuilder.append("CODE:= ${response.code} ---- MESSAGE:= ${response.message}\n")
                val source = response.body.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                buffer.clear()
                buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(response.headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val charsetResponse: Charset = response.body.contentType().charset()

                if (!buffer.isProbablyUtf8()) {
                    logBuilder.append("\n")
                    logBuilder.append("<-- END HTTP (binary ${buffer.size}-byte body omitted)\n \n \n \n")
                    Log.d(tag, logBuilder.toString())
                    logBuilder.clear()
                    return
                }

                if (response.body.contentLength() != 0L) {
                    logBuilder.append(buffer.clone().readString(charsetResponse).plus("\n"))
                }

                if (gzippedLength != null) {
                    logBuilder.append("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)\n")
                } else {
                    logBuilder.append("<-- END HTTP (${buffer.size}-byte body)\n")
                }

                logBuilder.append("\n \n \n ")
                Log.d(tag, logBuilder.toString())
                logBuilder.clear()
            }
        }
    }
}