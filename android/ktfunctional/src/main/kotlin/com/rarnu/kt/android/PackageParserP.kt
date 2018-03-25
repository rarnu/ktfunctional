package com.rarnu.kt.android

typealias PackageParser = Any
typealias Package = Any
typealias Activity = Any
typealias Service = Any
typealias Provider = Any
typealias Permission = Any
typealias PermissionGroup = Any
typealias Instrumentation = Any
typealias Component = Any

object PackageParserP {

    const val PARSE_IS_SYSTEM = 1 shl 0
    const val PARSE_CHATTY = 1 shl 1
    const val PARSE_MUST_BE_APK = 1 shl 2
    const val PARSE_IGNORE_PROCESSES = 1 shl 3
    const val PARSE_FORWARD_LOCK = 1 shl 4
    const val PARSE_EXTERNAL_STORAGE = 1 shl 5
    const val PARSE_IS_SYSTEM_DIR = 1 shl 6
    const val PARSE_IS_PRIVILEGED = 1 shl 7
    const val PARSE_COLLECT_CERTIFICATES = 1 shl 8
    const val PARSE_TRUSTED_OVERLAY = 1 shl 9
    const val PARSE_ENFORCE_CODE = 1 shl 10

    fun newPackageParser(): PackageParser? {
        var ret: PackageParser? = null
        try {
            val cPackageParser = Class.forName("android.content.pm.PackageParser")
            val mConstructor = cPackageParser.getDeclaredConstructor()
            mConstructor.isAccessible = true
            ret = mConstructor.newInstance()
        } catch (e: Throwable) {
        }
        return ret
    }

}

@Suppress("UNCHECKED_CAST")
internal object PackageParserRef {
    fun getStringValue(obj: Any, field: String): String? {
        var ret: String? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as String?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getStringValueSuper(obj: Any, field: String): String? {
        var ret: String? = null
        try {
            val f = obj.javaClass.superclass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as String?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getStringArrayValue(obj: Any, field: String): Array<String>? {
        var ret: Array<String>? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as Array<String>?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getIntValue(obj: Any, field: String, def: Int): Int {
        var ret = def
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.getInt(obj)
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getIntArrayValue(obj: Any, field: String): IntArray? {
        var ret: IntArray? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as IntArray?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getObjectValue(obj: Any, field: String): Any? {
        var ret: Any? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj)
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getObjectValueSuper(obj: Any, field: String): Any? {
        var ret: Any? = null
        try {
            val f = obj.javaClass.superclass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj)
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getBooleanValue(obj: Any, field: String, def: Boolean): Boolean {
        var ret = def
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.getBoolean(obj)
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getByteArrayValue(obj: Any, field: String): ByteArray? {
        var ret: ByteArray? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as ByteArray?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getStringListValue(obj: Any, field: String): List<String>? {
        var ret: List<String>? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as List<String>?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getObjectListValue(obj: Any, field: String): List<Any>? {
        var ret: List<Any>? = null
        try {
            val f = obj.javaClass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as List<Any>?
        } catch (e: Throwable) {

        }
        return ret
    }

    fun getObjectListValueSuper(obj: Any, field: String): List<Any>? {
        var ret: List<Any>? = null
        try {
            val f = obj.javaClass.superclass.getDeclaredField(field)
            f.isAccessible = true
            ret = f.get(obj) as List<Any>?
        } catch (e: Throwable) {

        }
        return ret
    }
}