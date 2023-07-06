package com.example.nailexpress.models.api


class ApiError(
    val statusCode: Int? = null,
    val message: String? = null,
    val error: String? = null,
    private val errors: List<Map<String, String>>? = null
) {
    private val contentError: String?
        get() {
            if (errors.isNullOrEmpty()) return null
            return errors[0].run {
                val key = keys.joinToString("\n")
                val error = values.joinToString("\n")
                error
            }
        }

    val body: String
        get() {
            val content = contentError
            if (content.isNullOrBlank()) return message ?: error ?: "Unknown"
            return "$message\n$content"
        }
}
