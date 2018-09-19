package com.rarnu.kt.android

import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager

val Context.appVersionCode: Int
    get() {
        var ret = 0
        try {
            val pi = packageManager.getPackageInfo(packageName, 0)
            ret = pi.longVersionCode.toInt()
        } catch (e: Exception) {

        }
        return ret
    }

val Context.appVersionName: String?
    get() {
        var ret: String? = null
        try {
            val pi = packageManager.getPackageInfo(packageName, 0)
            ret = pi.versionName
        } catch (e: Exception) {

        }
        return ret
    }

fun Context.readConfig(key: String, def: String?) = ContextOperations.pref(this).getString(key, def)
fun Context.writeConfig(key: String, value: String?) = ContextOperations.pref(this).edit().putString(key, value).apply()
fun Context.readConfig(key: String, def: Int) = ContextOperations.pref(this).getInt(key, def)
fun Context.writeConfig(key: String, value: Int) = ContextOperations.pref(this).edit().putInt(key, value).apply()
fun Context.readConfig(key: String, def: Float) = ContextOperations.pref(this).getFloat(key, def)
fun Context.writeConfig(key: String, value: Float) = ContextOperations.pref(this).edit().putFloat(key, value).apply()
fun Context.readConfig(key: String, def: Long) = ContextOperations.pref(this).getLong(key, def)
fun Context.writeConfig(key: String, value: Long) = ContextOperations.pref(this).edit().putLong(key, value).apply()
fun Context.readConfig(key: String, def: Boolean) = ContextOperations.pref(this).getBoolean(key, def)
fun Context.writeConfig(key: String, value: Boolean) = ContextOperations.pref(this).edit().putBoolean(key, value).apply()
fun Context.readMetaData(key: String, def: String?) = ContextOperations.metaData(this).getString(key, def)
fun Context.readMetaData(key: String, def: Int) = ContextOperations.metaData(this).getInt(key, def)
fun Context.readMetaData(key: String, def: Float) = ContextOperations.metaData(this).getFloat(key, def)
fun Context.readMetaData(key: String, def: Long) = ContextOperations.metaData(this).getLong(key, def)
fun Context.readMetaData(key: String, def: Boolean) = ContextOperations.metaData(this).getBoolean(key, def)

private object ContextOperations {
    fun pref(ctx: Context) = PreferenceManager.getDefaultSharedPreferences(ctx)!!
    fun metaData(ctx: Context) = ctx.packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA).metaData!!
}