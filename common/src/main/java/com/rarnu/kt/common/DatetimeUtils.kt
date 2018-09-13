package com.rarnu.kt.common

import java.util.*

fun Long.toCalendar(): Calendar {
    val c = Calendar.getInstance()
    c.timeInMillis = this
    return c
}

fun Calendar.addYear(year: Int): Calendar {
    add(Calendar.YEAR, year)
    return this
}

fun Calendar.addMonth(month: Int): Calendar {
    add(Calendar.MONTH, month)
    return this
}

fun Calendar.addDay(day: Int): Calendar {
    add(Calendar.DAY_OF_YEAR, day)
    return this
}

fun Calendar.addHour(hour: Int): Calendar {
    add(Calendar.HOUR_OF_DAY, hour)
    return this
}

fun Calendar.addMinute(minute: Int): Calendar {
    add(Calendar.MINUTE, minute)
    return this
}

fun Calendar.addSecond(second: Int): Calendar {
    add(Calendar.SECOND, second)
    return this
}

fun Calendar.addMilliSecond(millisecond: Int): Calendar {
    add(Calendar.MILLISECOND, millisecond)
    return this
}

fun Calendar.sameWeekdayInMonth(year: Int, month: Int): List<Int> {
    val weekThis = get(Calendar.DAY_OF_WEEK)
    val cNew = Calendar.getInstance()
    cNew.set(Calendar.YEAR, year)
    cNew.set(Calendar.MONTH, month)
    cNew.set(Calendar.DAY_OF_MONTH, 1)

    val listNow = mutableListOf<Int>()
    var weekNow: Int
    for (i in 0 until 7) {
        weekNow = cNew.get(Calendar.DAY_OF_WEEK)
        if (weekNow == weekThis) break
        cNew.add(Calendar.DAY_OF_MONTH, 1)
    }
    var currentDay = cNew.get(Calendar.DAY_OF_MONTH)
    while (currentDay <= 31) {
        listNow.add(currentDay)
        currentDay += 7
    }
    return listNow
}

fun Calendar.nextWeekday(): Calendar {
    val weekThen = get(Calendar.DAY_OF_WEEK)
    val cNow = Calendar.getInstance()
    cNow.add(Calendar.DAY_OF_MONTH, 1)
    var weekNow: Int
    for (i in 0 until 7) {
        weekNow = cNow.get(Calendar.DAY_OF_WEEK)
        if (weekNow == weekThen) break
        cNow.add(Calendar.DAY_OF_MONTH, 1)
    }
    return cNow
}

fun Calendar.priorWeekday(): Calendar {
    val weekThen = get(Calendar.DAY_OF_WEEK)
    val cNow = Calendar.getInstance()
    cNow.add(Calendar.DAY_OF_MONTH, -1)
    var weekNow: Int
    for (i in 0 until 7) {
        weekNow = cNow.get(Calendar.DAY_OF_WEEK)
        if (weekNow == weekThen) break
        cNow.add(Calendar.DAY_OF_MONTH, -1)
    }
    return cNow
}
