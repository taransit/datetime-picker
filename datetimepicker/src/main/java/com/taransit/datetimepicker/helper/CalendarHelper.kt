package com.taransit.datetimepicker.helper

import java.util.*

object CalendarHelper {

    fun createDayStartCalendar(year: Int, month: Int, dayOfMonth: Int): Calendar {
        return Calendar.getInstance()
            .apply { set(year, month, dayOfMonth) }
            .let(::createDayStartCalendar)
    }

    fun createDayStartCalendar(calendar: Calendar): Calendar {
        return calendar.apply {
            set(Calendar.AM_PM, 0)
            set(Calendar.HOUR, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
}