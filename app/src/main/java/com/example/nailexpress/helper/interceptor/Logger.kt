package com.example.nailexpress.helper.interceptor

import com.example.nailexpress.extension.safe
import okhttp3.Request
import java.io.IOException
import java.util.logging.Level

interface BufferListener {
    @Throws(IOException::class)
    fun getJsonResponse(var1: Request?): String?
}

interface Logger {

    enum class Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    fun log(level: Int, tag: String, message: String, useLogHack: Boolean)

    companion object {

        internal var DEFAULT: Logger = DefaultLogger()
    }
}

class DefaultLogger : Logger {

    override fun log(level: Int, tag: String, message: String, useLogHack: Boolean) {
        val finalTag = getFinalTag(tag.safe(), useLogHack)
        val logger = java.util.logging.Logger.getLogger(if (useLogHack) finalTag else tag)
        when (level) {
            4 -> logger.log(Level.INFO, message)
            else -> logger.log(Level.WARNING, message)
        }
    }

    companion object {
        private val prefix = arrayOf(". ", " .")
        private var index = 0
        private fun getFinalTag(tag: String, isLogHackEnable: Boolean): String {
            return if (isLogHackEnable) {
                index = index xor 1
                prefix[index] + tag
            } else {
                tag
            }
        }
    }
}