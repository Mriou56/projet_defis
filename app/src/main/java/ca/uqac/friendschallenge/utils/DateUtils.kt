package ca.uqac.friendschallenge.utils

import java.util.Calendar

fun isWeekend(): Boolean {
    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
}

