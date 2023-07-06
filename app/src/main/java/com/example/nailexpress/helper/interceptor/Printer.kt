package com.example.nailexpress.helper.interceptor

import android.text.TextUtils
import com.example.nailexpress.extension.safe
import okhttp3.Request
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

internal open class Printer protected constructor() {
    companion object {
        private const val JSON_INDENT = 3
        private val LINE_SEPARATOR = System.getProperty("line.separator").safe()
        private var DOUBLE_SEPARATOR: String? = null
        private val OMITTED_RESPONSE: Array<String>
        private val OMITTED_REQUEST: Array<String>
        private const val N = "\n"
        private const val T = "\t"
        private const val REQUEST_UP_LINE =
            "┌────── Request ────────────────────────────────────────────────────────────────────────"
        private const val END_LINE =
            "└───────────────────────────────────────────────────────────────────────────────────────"
        private const val RESPONSE_UP_LINE =
            "┌────── Response ───────────────────────────────────────────────────────────────────────"
        private const val BODY_TAG = "Body:"
        private const val URL_TAG = "URL: "
        private const val METHOD_TAG = "Method: @"
        private const val HEADERS_TAG = "Headers:"
        private const val STATUS_CODE_TAG = "Status Code: "
        private const val RECEIVED_TAG = "Received in: "
        private const val CORNER_UP = "┌ "
        private const val CORNER_BOTTOM = "└ "
        private const val CENTER_LINE = "├ "
        private const val DEFAULT_LINE = "│ "
        private var OOM_OMITTED: String? = null
        private fun isEmpty(line: String): Boolean {
            return TextUtils.isEmpty(line) || N == line || T == line || TextUtils.isEmpty(line.trim { it <= ' ' })
        }

        fun printJsonRequest(builder: LoggingInterceptor.Builder, request: Request) {
            val requestBody = "$LINE_SEPARATOR$BODY_TAG$LINE_SEPARATOR${bodyToString(request)}"
            val tag = builder.getTag(true)
            builder.getLogger().log(
                builder.getType(),
                tag,
                REQUEST_UP_LINE,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                arrayOf(URL_TAG + request.url),
                builder.getLogger(),
                false,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                getRequest(request, builder.getLevel()),
                builder.getLogger(),
                true,
                builder.isLogHackEnable()
            )
            if (builder.getLevel() === Logger.Level.BASIC || builder.getLevel() === Logger.Level.BODY) {
                logLines(
                    builder.getType(),
                    tag,
                    requestBody.split(LINE_SEPARATOR).toTypedArray(),
                    builder.getLogger(),
                    true,
                    builder.isLogHackEnable()
                )
            }
            builder.getLogger().log(
                builder.getType(),
                tag,
                END_LINE,
                builder.isLogHackEnable()
            )
        }

        fun printJsonResponse(
            builder: LoggingInterceptor.Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            bodyString: String,
            segments: List<String>,
            message: String,
            responseUrl: String
        ) {
            val responseBody = "$LINE_SEPARATOR$BODY_TAG$LINE_SEPARATOR${getJsonString(bodyString)}"
            val tag = builder.getTag(false)
            val urlLine = arrayOf("$URL_TAG$responseUrl", N)
            val response = getResponse(
                headers,
                chainMs,
                code,
                isSuccessful,
                builder.getLevel(),
                segments,
                message
            )
            builder.getLogger().log(
                builder.getType(),
                tag,
                RESPONSE_UP_LINE,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                urlLine,
                builder.getLogger(),
                true,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                response,
                builder.getLogger(),
                true,
                builder.isLogHackEnable()
            )
            if (builder.getLevel() === Logger.Level.BASIC || builder.getLevel() === Logger.Level.BODY) {
                logLines(
                    builder.getType(),
                    tag,
                    responseBody.split(LINE_SEPARATOR).toTypedArray(),
                    builder.getLogger(),
                    true,
                    builder.isLogHackEnable()
                )
            }
            builder.getLogger().log(
                builder.getType(),
                tag,
                END_LINE,
                builder.isLogHackEnable()
            )
        }

        fun printFileRequest(builder: LoggingInterceptor.Builder, request: Request) {
            val tag = builder.getTag(true)
            builder.getLogger().log(
                builder.getType(),
                tag,
                REQUEST_UP_LINE,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                arrayOf(URL_TAG + request.url),
                builder.getLogger(),
                false,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                getRequest(request, builder.getLevel()),
                builder.getLogger(),
                true,
                builder.isLogHackEnable()
            )
            if (builder.getLevel() === Logger.Level.BASIC || builder.getLevel() === Logger.Level.BODY) {
                logLines(
                    builder.getType(),
                    tag,
                    OMITTED_REQUEST,
                    builder.getLogger(),
                    true,
                    builder.isLogHackEnable()
                )
            }
            builder.getLogger().log(
                builder.getType(),
                tag,
                END_LINE,
                builder.isLogHackEnable()
            )
        }

        fun printFileResponse(
            builder: LoggingInterceptor.Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            segments: List<String>,
            message: String
        ) {
            val tag = builder.getTag(false)
            builder.getLogger().log(
                builder.getType(),
                tag,
                "┌────── Response ───────────────────────────────────────────────────────────────────────",
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                getResponse(
                    headers,
                    chainMs,
                    code,
                    isSuccessful,
                    builder.getLevel(),
                    segments,
                    message
                ),
                builder.getLogger(),
                true,
                builder.isLogHackEnable()
            )
            logLines(
                builder.getType(),
                tag,
                OMITTED_RESPONSE,
                builder.getLogger(),
                true,
                builder.isLogHackEnable()
            )
            builder.getLogger().log(
                builder.getType(),
                tag,
                END_LINE,
                builder.isLogHackEnable()
            )
        }

        private fun getRequest(request: Request, level: Logger.Level): Array<String> {
            val header = request.headers.toString()
            val loggableHeader = level === Logger.Level.HEADERS || level === Logger.Level.BASIC
            val log =
                METHOD_TAG + request.method + DOUBLE_SEPARATOR + when {
                    isEmpty(header) -> ""
                    loggableHeader -> "Headers:$LINE_SEPARATOR${dotHeaders(header)}"
                    else -> ""
                }
            return log.split(LINE_SEPARATOR).toTypedArray()
        }

        private fun getResponse(
            header: String,
            tookMs: Long,
            code: Int,
            isSuccessful: Boolean,
            level: Logger.Level,
            segments: List<String>,
            message: String
        ): Array<String> {
            val loggableHeader = level === Logger.Level.HEADERS || level === Logger.Level.BASIC
            val segmentString = slashSegments(segments)
            val log =
                (if (!TextUtils.isEmpty(segmentString)) "$segmentString - " else "") + "is success : " + isSuccessful + " - " + RECEIVED_TAG + tookMs + "ms" + DOUBLE_SEPARATOR + STATUS_CODE_TAG + code + " / " + message + DOUBLE_SEPARATOR + when {
                    isEmpty(header) -> ""
                    loggableHeader -> HEADERS_TAG + LINE_SEPARATOR + dotHeaders(header)
                    else -> ""
                }
            return log.split(LINE_SEPARATOR).toTypedArray()
        }

        private fun slashSegments(segments: List<String>): String {
            val segmentString = StringBuilder()
            for (segment in segments) segmentString.append("/").append(segment)
            return segmentString.toString()
        }

        private fun dotHeaders(header: String): String {
            val headers = header.split(LINE_SEPARATOR).toTypedArray()
            val builder = StringBuilder()
            var tag = "─ "
            if (headers.size > 1) {
                for (i in headers.indices) {
                    tag = when (i) {
                        0 -> CORNER_UP
                        headers.size - 1 -> CORNER_BOTTOM
                        else -> CENTER_LINE
                    }
                    builder.append(tag).append(headers[i]).append(N)
                }
            } else {
                for (item in headers) {
                    builder.append(tag).append(item).append(N)
                }
            }
            return builder.toString()
        }

        private fun logLines(
            type: Int,
            tag: String,
            lines: Array<String>,
            logger: Logger,
            withLineSize: Boolean,
            useLogHack: Boolean
        ) {
            for (line in lines) {
                val lineLength = line.length
                val maxLongSize = if (withLineSize) 110 else lineLength
                for (i in 0..lineLength / maxLongSize) {
                    val start = i * maxLongSize
                    var end = (i + 1) * maxLongSize
                    end = if (end > line.length) line.length else end
                    logger.log(type, tag, DEFAULT_LINE + line.substring(start, end), useLogHack)
                }
            }
        }

        private fun bodyToString(request: Request): String? {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                val body = copy.body
                if (body == null) {
                    ""
                } else {
                    body.writeTo(buffer)
                    getJsonString(buffer.readUtf8())
                }
            } catch (e: IOException) {
                "{\"err\": \"" + e.message + "\"}"
            }
        }

        fun getJsonString(msg: String): String? {
            return try {
                when {
                    msg.startsWith("{") -> {
                        val jsonObject = JSONObject(msg)
                        jsonObject.toString(JSON_INDENT)
                    }
                    msg.startsWith("[") -> {
                        val jsonArray = JSONArray(msg)
                        jsonArray.toString(JSON_INDENT)
                    }
                    else -> {
                        msg
                    }
                }
            } catch (var3: JSONException) {
                msg
            } catch (var4: OutOfMemoryError) {
                OOM_OMITTED
            }
        }

        init {
            DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR
            OMITTED_RESPONSE = arrayOf(LINE_SEPARATOR, "Omitted response body")
            OMITTED_REQUEST = arrayOf(LINE_SEPARATOR, "Omitted request body")
            OOM_OMITTED = LINE_SEPARATOR + "Output omitted because of Object size."
        }
    }

    init {
        throw UnsupportedOperationException()
    }
}
