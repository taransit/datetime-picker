package com.taransit.datetimepicker

enum class Direction(val value: Int) {

    UNKNOWN(-1),
    DEPARTURE(0),
    ARRIVAL(1);

    companion object {
        fun from(value: Int): Direction {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}