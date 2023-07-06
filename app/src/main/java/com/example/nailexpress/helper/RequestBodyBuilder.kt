package com.example.nailexpress.helper

import android.util.Log
import com.example.nailexpress.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RequestBodyBuilder {
    private val mRequest = hashMapOf<String, String>()

    fun put(field: String, value: String): RequestBodyBuilder {
        mRequest[field] = value
        Log.e(field, value)
        return this
    }

    fun putIf(condition: Boolean, field: String, value: Any): RequestBodyBuilder {
        if(condition){
            mRequest[field] = value.toString()
            Log.e(field, value.toString())
        }
        return this
    }

    fun put(field: String, value: Any?): RequestBodyBuilder {
        if(value==null)
            return this
        var input=value.toString()
        mRequest[field] = input
        Log.e(field, input)
        return this
    }

    fun build(): Map<String, String> {
        return mRequest
    }

    infix fun String.toImagePart(url: String?) = createImagePart(this, url)

    private fun createImagePart(field: String, url: String?): MultipartBody.Part? {
        if (url == null) return null
        val file = File(url)
        if (!file.exists()) return null
        val type = FileUtils.getMimeType(file.path) ?: return null
        return MultipartBody.Part.createFormData(field, file.name,
                RequestBody.create(type.toMediaTypeOrNull(), file))
    }
    fun buildQuery(): String {
        val stringBuilder = StringBuilder()
        for (field in mRequest.keys) {
            stringBuilder.append(field)
                .append("=")
                .append(mRequest[field])
                .append("&")
        }
        if (stringBuilder.isNotEmpty())
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        return stringBuilder.toString()
    }
}
