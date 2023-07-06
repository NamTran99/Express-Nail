package com.example.nailexpress.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream


object FileUtils {
    /**
     * Get mime type of file
     *
     * @param url
     * @return Type of file Ex: For image: Png, JPG...
     */
    fun getMimeType(url: String): String? {
        var type: String? = null
        var extension = ""
        val i: Int = url.lastIndexOf('.')
        if (i > 0) {
            extension = url.substring(i + 1)
        }
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return type
    }

    /**
     * Method for return file path of Gallery image/ Document / Video / Audio
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            when {
                isExternalStorageDocument(uri) -> return getExternalPath(uri)
                isDownloadsDocument(uri) -> return getPathInDownloads(context, uri)
                isMediaDocument(uri) -> return getPathInDocuments(context, uri)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getContentPath(context, uri)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return uri.path
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getPathInDocuments(context: Context, uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        var contentUri: Uri? = null
        when (type) {
            "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val selection = "_id=?"
        val selectionArgs = arrayOf(split[1])

        return getDataColumn(context, contentUri, selection,
                selectionArgs)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getPathInDownloads(context: Context, uri: Uri): String? {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id))

        return getDataColumn(context, contentUri, null, null)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getExternalPath(uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        if ("primary".equals(type, ignoreCase = true)) {
            return "${Environment.getExternalStorageDirectory()}/${split[1]}"
        }
        return null
    }

    private fun getContentPath(context: Context, uri: Uri): String? {
        return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     * The context.
     * @param uri
     * The Uri to query.
     * @param selection
     * (Optional) Filter used in the query.
     * @param selectionArgs
     * (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    @JvmStatic
    fun getDataColumn(context: Context, uri: Uri?,
                      selection: String?, selectionArgs: Array<String>?): String? {
        if (uri == null) return null
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        val path: String?

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor == null) return null
            if (!cursor.moveToFirst()) return null
            val index = cursor.getColumnIndexOrThrow(column)
            path = cursor.getString(index)
        } finally {
            cursor?.close()
        }
        return path
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    @JvmStatic
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
                .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    @JvmStatic
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
                .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    @JvmStatic
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
                .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    @JvmStatic
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
                .authority
    }

    fun save(context: Context, it: Bitmap): Uri {
        val currentTime = System.currentTimeMillis()
        val path = "${context.cacheDir}/$currentTime.jpg"
        FileOutputStream(path).use { out ->
            it.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return Uri.fromFile(File(path))
    }
}