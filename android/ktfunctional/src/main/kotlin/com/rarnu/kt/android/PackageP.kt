package com.rarnu.kt.android

import android.content.ComponentName
import android.content.IntentFilter
import android.content.pm.*
import android.os.Bundle
import java.io.File

/**
 * ******************************************
 * ******************************************
 * INNER CLASS PACKAGEPARSER
 * ******************************************
 * ******************************************
 */

fun PackageParser.parsePackage(path: String, flag: Int) = parsePackage(File(path), flag)

fun PackageParser.parsePackage(file: File, flag: Int): Package? {
    var ret: Package? = null
    try {
        val mParse = javaClass.getDeclaredMethod("parsePackage", File::class.java, Integer.TYPE)
        mParse.isAccessible = true
        ret = mParse.invoke(this, file, flag)
    } catch (e: Throwable) {
    }
    return ret
}

fun PackageParser.collectCertificates(pkg: Package, parseFlags: Int) {
    try {
        val cPackage = Class.forName("android.content.pm.PackageParser\$Package")
        val mCollectCertificates = javaClass.getDeclaredMethod("collectCertificates", cPackage, Int::class.java)
        mCollectCertificates.isAccessible = true
        mCollectCertificates.invoke(this, pkg, parseFlags)
    } catch (e: Throwable) {

    }
}

/**
 * ******************************************
 * ******************************************
 * INNER CLASS PACKAGE
 * ******************************************
 * ******************************************
 */

val Package.applicationInfo: ApplicationInfo?
    get() = PackageParserRef.getObjectValue(this, "applicationInfo") as ApplicationInfo?

val Package.packageName: String?
    get() = PackageParserRef.getStringValue(this, "packageName")

val Package.splitNames: Array<String>?
    get() = PackageParserRef.getStringArrayValue(this, "splitNames")

val Package.volumeUuid: String?
    get() = PackageParserRef.getStringValue(this, "volumeUuid")

val Package.codePath: String?
    get() = PackageParserRef.getStringValue(this, "codePath")

val Package.baseCodePath: String?
    get() = PackageParserRef.getStringValue(this, "baseCodePath")

val Package.splitCodePaths: Array<String>?
    get() = PackageParserRef.getStringArrayValue(this, "splitCodePaths")

val Package.baseRevisionCode: Int
    get() = PackageParserRef.getIntValue(this, "baseRevisionCode", -1)

val Package.splitRevisionCodes: IntArray?
    get() = PackageParserRef.getIntArrayValue(this, "splitRevisionCodes")

val Package.splitFlags: IntArray?
    get() = PackageParserRef.getIntArrayValue(this, "splitFlags")

val Package.splitPrivateFlags: IntArray?
    get() = PackageParserRef.getIntArrayValue(this, "splitPrivateFlags")

val Package.baseHardwareAccelerated: Boolean
    get() = PackageParserRef.getBooleanValue(this, "baseHardwareAccelerated", false)

/**
 * @return Package
 */
val Package.parentPackage: Package?
    get() = PackageParserRef.getObjectValue(this, "parentPackage")

/**
 * @return List<Package>
 */
val Package.childPackages: List<Package>?
    get() = PackageParserRef.getObjectListValue(this, "childPackages")

val Package.staticSharedLibName: String?
    get() = PackageParserRef.getStringValue(this, "staticSharedLibName")

val Package.staticSharedLibVersion: Int
    get() = PackageParserRef.getIntValue(this, "staticSharedLibVersion", 0)

val Package.usesStaticLibrariesVersions: IntArray?
    get() = PackageParserRef.getIntArrayValue(this, "usesStaticLibrariesVersions")

val Package.usesStaticLibrariesCertDigests: Array<String>?
    get() = PackageParserRef.getStringArrayValue(this, "usesStaticLibrariesCertDigests")

val Package.usesLibraryFiles: Array<String>?
    get() = PackageParserRef.getStringArrayValue(this, "usesLibraryFiles")

val Package.installLocation: Int
    get() = PackageParserRef.getIntValue(this, "installLocation", 0)

val Package.coreApp: Boolean
    get() = PackageParserRef.getBooleanValue(this, "coreApp", false)

val Package.cpuAbiOverride: String?
    get() = PackageParserRef.getStringValue(this, "cpuAbiOverride")

val Package.use32bitAbi: Boolean
    get() = PackageParserRef.getBooleanValue(this, "use32bitAbi", false)

val Package.restrictUpdateHash: ByteArray?
    get() = PackageParserRef.getByteArrayValue(this, "restrictUpdateHash")

val Package.visibleToInstantApps: Boolean
    get() = PackageParserRef.getBooleanValue(this, "visibleToInstantApps", false)

val Package.requestedPermissions: List<String>?
    get() = PackageParserRef.getStringListValue(this, "requestedPermissions")

val Package.protectedBroadcasts: List<String>?
    get() = PackageParserRef.getStringListValue(this, "protectedBroadcasts")

val Package.libraryNames: List<String>?
    get() = PackageParserRef.getStringListValue(this, "libraryNames")

val Package.usesLibraries: List<String>?
    get() = PackageParserRef.getStringListValue(this, "usesLibraries")

val Package.usesStaticLibraries: List<String>?
    get() = PackageParserRef.getStringListValue(this, "usesStaticLibraries")

val Package.usesOptionalLibraries: List<String>?
    get() = PackageParserRef.getStringListValue(this, "usesOptionalLibraries")

/**
 * @return List<Activity>
 */
val Package.activities: List<Activity>?
    get() = PackageParserRef.getObjectListValue(this, "activities")

/**
 * @return List<Activity>
 */
val Package.receivers: List<Activity>?
    get() = PackageParserRef.getObjectListValue(this, "receivers")

/**
 * @return List<Service>
 */
val Package.services: List<Service>?
    get() = PackageParserRef.getObjectListValue(this, "services")

/**
 * @return List<Provider>
 */
val Package.providers: List<Provider>?
    get() = PackageParserRef.getObjectListValue(this, "providers")

/**
 * @return List<Permission>
 */
val Package.permissions: List<Permission>?
    get() = PackageParserRef.getObjectListValue(this, "permissions")

/**
 * @return List<PermissionGroup>
 */
val Package.permissionGroups: List<PermissionGroup>?
    get() = PackageParserRef.getObjectListValue(this, "permissionGroups")

/**
 * @return List<Instrumentation>
 */
val Package.instrumentation: List<Instrumentation>?
    get() = PackageParserRef.getObjectListValue(this, "instrumentation")

@Suppress("UNCHECKED_CAST")
val Package.reqFeatures: List<FeatureInfo>?
    get() = PackageParserRef.getObjectListValue(this, "reqFeatures") as List<FeatureInfo>?

@Suppress("UNCHECKED_CAST")
val Package.featureGroups: List<FeatureGroupInfo>?
    get() = PackageParserRef.getObjectListValue(this, "featureGroups") as List<FeatureGroupInfo>?

@Suppress("UNCHECKED_CAST")
val Package.configPreferences: List<ConfigurationInfo>?
    get() = PackageParserRef.getObjectListValue(this, "configPreferences") as List<ConfigurationInfo>?


/**
 * ******************************************
 * ******************************************
 * INNER CLASS ACTIVITY
 * ******************************************
 * ******************************************
 */

val Activity.activityInfo: ActivityInfo?
    get() = PackageParserRef.getObjectValue(this, "info") as ActivityInfo?

/**
 * ******************************************
 * ******************************************
 * INNER CLASS SERVICE
 * ******************************************
 * ******************************************
 */

val Service.serviceInfo: ServiceInfo?
    get() = PackageParserRef.getObjectValue(this, "info") as ServiceInfo?

/**
 * ******************************************
 * ******************************************
 * INNER CLASS PROVIDER
 * ******************************************
 * ******************************************
 */

val Provider.providerInfo: ProviderInfo?
    get() = PackageParserRef.getObjectValue(this, "info") as ProviderInfo?


val Provider.providerSyncable: Boolean
    get() = PackageParserRef.getBooleanValue(this, "syncable", false)


/**
 * ******************************************
 * ******************************************
 * INNER CLASS PERMISSION
 * ******************************************
 * ******************************************
 */

val Permission.permissionInfo: PermissionInfo?
    get() = PackageParserRef.getObjectValue(this, "info") as PermissionInfo?


val Permission.permissionTree: Boolean
    get() = PackageParserRef.getBooleanValue(this, "tree", false)

/**
 * @return PermissionGroup
 */
val Permission.permissionGroup: PermissionGroup?
    get() = PackageParserRef.getObjectValue(this, "group")

/**
 * ******************************************
 * ******************************************
 * INNER CLASS PERMISSIONGROUP
 * ******************************************
 * ******************************************
 */

val PermissionGroup.permissionGroupInfo: PermissionGroupInfo?
    get() = PackageParserRef.getObjectValue(this, "info") as PermissionGroupInfo?

/**
 * ******************************************
 * ******************************************
 * INNER CLASS INSTRUMENTATION
 * ******************************************
 * ******************************************
 */


val Instrumentation.instrumentationInfo: InstrumentationInfo?
    get() = PackageParserRef.getObjectValue(this, "info") as InstrumentationInfo?

/**
 * ******************************************
 * ******************************************
 * INNER CLASS COMPONENT
 * ******************************************
 * ******************************************
 */

// ==============================================================
// All methods here must be called when "isActivity"
// or "isService" or "isProvider" or "isPermission"
// or "isPermissionGroup" or "isInstrumentation" equals true
// ==============================================================

/**
 * @return List<IntentFilter>
 */
@Suppress("UNCHECKED_CAST")
val Component.intents: List<IntentFilter>?
    get() = PackageParserRef.getObjectListValueSuper(this, "intents") as List<IntentFilter>?

val Component.className: String?
    get() = PackageParserRef.getStringValueSuper(this, "className")

val Component.metaData: Bundle?
    get() = PackageParserRef.getObjectValueSuper(this, "metaData") as Bundle?

/**
 * @return Package
 */
val Component.owner: Package?
    get() = PackageParserRef.getObjectValueSuper(this, "owner")

val Component.componentName: ComponentName?
    get() = PackageParserRef.getObjectValueSuper(this, "componentName") as ComponentName?

val Component.componentShortName: String?
    get() = PackageParserRef.getStringValueSuper(this, "componentShortName")
