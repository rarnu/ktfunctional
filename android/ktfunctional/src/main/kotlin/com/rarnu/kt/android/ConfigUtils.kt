package com.rarnu.kt.android

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.preference.PreferenceManager

fun readFromPreference(key: String, def: String?) = ConfigUtils.sp.getString(key, def)
fun String?.writeToPreference(key: String) = ConfigUtils.sp.edit().putString(key, this).apply()
fun readFromPreference(key: String, def: Int) = ConfigUtils.sp.getInt(key, def)
fun Int.writeToPreference(key: String) = ConfigUtils.sp.edit().putInt(key, this).apply()
fun readFromPreference(key: String, def: Float) = ConfigUtils.sp.getFloat(key, def)
fun Float.writeToPreference(key: String) = ConfigUtils.sp.edit().putFloat(key, this).apply()
fun readFromPreference(key: String, def: Long) = ConfigUtils.sp.getLong(key, def)
fun Long.writeToPreference(key: String) = ConfigUtils.sp.edit().putLong(key, this).apply()
fun readFromPreference(key: String, def: Boolean) = ConfigUtils.sp.getBoolean(key, def)
fun Boolean.writeToPreference(key: String) = ConfigUtils.sp.edit().putBoolean(key, this).apply()

fun readFromManifest(ctx: Context, key: String, def: String?) = ConfigUtils.getAppInfo(ctx).metaData?.getString(key, def)
fun readFromManifest(ctx: Context, key: String, def: Int) = ConfigUtils.getAppInfo(ctx).metaData?.getInt(key, def)
fun readFromManifest(ctx: Context, key: String, def: Float) = ConfigUtils.getAppInfo(ctx).metaData?.getFloat(key, def)
fun readFromManifest(ctx: Context, key: String, def: Long) = ConfigUtils.getAppInfo(ctx).metaData?.getLong(key, def)
fun readFromManifest(ctx: Context, key: String, def: Boolean) = ConfigUtils.getAppInfo(ctx).metaData?.getBoolean(key, def)

private object ConfigUtils {

    internal lateinit var sp: SharedPreferences

    fun init(ctx: Context) {
        sp = PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    internal fun getAppInfo(ctx: Context) = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
}