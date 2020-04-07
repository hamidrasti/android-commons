package ir.hamraa.androidcommons.core

import android.content.Context

object TinyDB {

    @JvmStatic
    fun putString(context: Context, key: String, value: String?, prefsName: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    @JvmStatic
    fun getString(context: Context, key: String, defValue: String?, prefsName: String): String? {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getString(key, defValue)
    }

    @JvmStatic
    fun putInt(context: Context, key: String, value: Int, prefsName: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

    @JvmStatic
    fun getInt(context: Context, key: String, defValue: Int, prefsName: String): Int {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getInt(key, defValue)
    }

    @JvmStatic
    fun putBoolean(context: Context, key: String, value: Boolean, prefsName: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }

    @JvmStatic
    fun getBoolean(context: Context, key: String, defValue: Boolean, prefsName: String): Boolean {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, defValue)
    }

    @JvmStatic
    fun putFloat(context: Context, key: String, value: Float, prefsName: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putFloat(key, value).apply()
    }

    @JvmStatic
    fun getFloat(context: Context, key: String, defValue: Float, prefsName: String): Float {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getFloat(key, defValue)
    }

    @JvmStatic
    fun putLong(context: Context, key: String, value: Long, prefsName: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().putLong(key, value).apply()
    }

    @JvmStatic
    fun getLong(context: Context, key: String, defValue: Long, prefsName: String): Long {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.getLong(key, defValue)
    }

    @JvmStatic
    fun contains(context: Context, key: String, prefsName: String): Boolean {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.contains(key)
    }

    @JvmStatic
    fun clear(context: Context, prefsName: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}