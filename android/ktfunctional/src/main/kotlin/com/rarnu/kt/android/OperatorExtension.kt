package com.rarnu.kt.android

import android.annotation.TargetApi
import android.os.Build

inline operator fun <reified T> Array<T>.minus(arr: Array<T>) = this.filter { !arr.contains(it) }.toTypedArray()
inline operator fun <reified K, V> Map<K, V>.minus(map: Map<K, V>) = this.filter { !map.contains(it.key) }.toMutableMap()
inline operator fun <reified K, V> Map<K, V>.minus(keys: List<K>) = this.filter { !keys.contains(it.key) }.toMutableMap()
inline operator fun <reified T> List<T>.minus(list: List<T>) = this.filter { !list.contains(it) }.toMutableList()
inline operator fun <reified T> Set<T>.minus(set: Set<T>) = this.filter { !set.contains(it) }.toMutableSet()

@TargetApi(Build.VERSION_CODES.N)
inline operator fun <reified T> MutableCollection<T>.minusAssign(list: List<T>) {
    this.removeIf { it in list }
}