package com.example.nailexpress.datasource.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharePrefs private constructor(context: Context) {
    private lateinit var _sharedPref: SharedPreferences
    private lateinit var _editor: SharedPreferences.Editor
    val gson by lazy { Gson() }
    val sharedPref
        get() = _sharedPref

    /**
     * init instance of [@see sharedPref] or [@see editor]
     */
    init {
        _sharedPref = context.getSharedPreferences(this.javaClass.name, Context.MODE_PRIVATE)
        _editor = _sharedPref.edit()
    }

    /**
     * @see _editor null -> throw errors
     * @param key earch value is save from key
     * @param data is value any
     */
    fun <T> put(key: String, data: T) {
        when (data) {
            is String -> _editor.putString(key, data)
            is Boolean -> _editor.putBoolean(key, data)
            is Float -> _editor.putFloat(key, data)
            is Double -> _editor.putFloat(key, data.toFloat())
            is Int -> _editor.putInt(key, data)
            is Long -> _editor.putLong(key, data)
            else -> _editor.putString(key, gson.toJson(data))
        }
        _editor.apply()
    }

    /**
     * @see _sharedPref null -> throw errors
     * @param key each is save from key
     * rely on the key to get the previously saved value
     */
    inline fun <reified T> get(key: String): T? {

        return when (T::class) {
            String::class -> sharedPref.getString(key, "")
            Boolean::class -> sharedPref.getBoolean(key, false)
            Float::class -> sharedPref.getFloat(key, FLOAT_DEFAULT)
            Double::class -> sharedPref.getFloat(key, FLOAT_DEFAULT)
            Int::class -> sharedPref.getInt(key, INT_DEFAULT)
            Long::class -> sharedPref.getLong(key, LONG_DEFAULT)
            else -> sharedPref.getString(key, EMPTY)?.let {
                if (it == EMPTY) {
                    return null
                }
                return gson.fromJson(it, T::class.java)
            }
        } as? T
    }

    /**
     * @see _editor null -> throw errors
     * remove list key the saved keys
     */
    fun remove(vararg key: String) {
        key.forEach {
            _editor.remove(it)?.commit()
        }
    }

    /**
     * @see _editor null -> throw errors
     * delete all the saved keys
     */
    fun removeAll() {
        _editor.clear()?.commit()
    }

    companion object {
        @Volatile
        private var instance: SharePrefs? = null

        const val EMPTY = ""
        const val FLOAT_DEFAULT = -1f
        const val INT_DEFAULT = -1
        const val LONG_DEFAULT = -1L

        fun getInstance(context: Context): SharePrefs = synchronized(this) {
            instance ?: SharePrefs(context).also { instance = it }
        }
    }
}

object SharePrefKey {
    const val USER_DTO: String = "user_dto"
    const val TOKEN: String = "token"
    const val APP_ROLE: String = "AppRole"
}