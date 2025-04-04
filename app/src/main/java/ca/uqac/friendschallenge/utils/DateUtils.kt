package ca.uqac.friendschallenge.utils

import java.util.Calendar
import java.util.Date

fun isWeekend(): Boolean {
    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
}

fun isDateInCurrentWeek(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = date

    val currentCalendar = Calendar.getInstance()
    val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
    val currentWeekOfYear = currentCalendar.get(Calendar.WEEK_OF_YEAR)
    val year = calendar.get(Calendar.YEAR)
    val currentYear = currentCalendar.get(Calendar.YEAR)

    return weekOfYear == currentWeekOfYear && year == currentYear
}

