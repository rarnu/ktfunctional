package com.rarnu.kt.android

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager

object System {
    val rooted = CommandOperations.rooted
    val busyboxInstalled = CommandOperations.busyboxInstalled
    fun isEmulator(ctx: Context) = SystemOperations.isEmulator(ctx)
}

private object SystemOperations {

    fun isEmulator(context: Context): Boolean = try {
        var ret = false
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imei = if (Build.VERSION.SDK_INT >= 26) tm.imei else ""
            if (imei != null && imei == "000000000000000") {
                ret = true
            }
        }

        if ((if (Build.VERSION.SDK_INT >= 26) Build.getSerial() else "") == "" || Build.PRODUCT.contains("sdk")) {
            ret = true
        }
        if (!ret) {
            ret = isBlueStacks()
        }
        if (!ret) {
            ret = isGenymotion()
        }
        ret
    } catch (e: Exception) {
        false
    }

    private fun isBlueStacks(): Boolean = Build.MODEL.toLowerCase().contains("bluestacks")

    private fun isGenymotion(): Boolean = Build.MODEL.toLowerCase().contains("genymotion")
}