package com.digital.digitalclock.core

import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Function-layer time data. This is the single source of "what time is it" that all
 * themes render from. Fields are resolved against the user-selected time zone so the
 * displayed time follows the chosen zone rather than the device zone.
 *
 * Uses [TimeZone] (not java.time.ZoneId) so it works down to the project's minSdk
 * without core-library desugaring.
 */
data class ClockTime(
    val hour24: Int,
    val minute: Int,
    val second: Int,
    val isPm: Boolean,
    val hour12: Int,
    val dateStr: String,
    val dayOfWeek: String,
    val colonLit: Boolean,
    val zoneLabel: String
) {
    /** The hour value to show for the given 12/24h mode (1-12 or 0-23). */
    fun displayHour(is24Hour: Boolean): Int = if (is24Hour) hour24 else hour12

    /** Zero-padded two-digit minute. */
    val minuteStr: String get() = String.format(Locale.getDefault(), "%02d", minute)

    /** Zero-padded two-digit second. */
    val secondStr: String get() = String.format(Locale.getDefault(), "%02d", second)

    /**
     * Two-character hour slots. In 12h mode a single-digit hour leaves the leading
     * slot blank (mirrors classic digital-clock behavior); in 24h mode it is padded.
     */
    fun hourSlots(is24Hour: Boolean): Pair<Char, Char> {
        val h = displayHour(is24Hour)
        return if (!is24Hour && h < 10) {
            ' ' to ('0' + h)
        } else {
            val s = String.format(Locale.getDefault(), "%02d", h)
            s[0] to s[1]
        }
    }
}

private val DAYS_OF_WEEK = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

/** Build the current [ClockTime] resolved against the zone with id [zoneId]. */
fun buildClockTime(zoneId: String): ClockTime {
    val zone = safeZone(zoneId)
    val cal = Calendar.getInstance(zone)
    val hour24 = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    val second = cal.get(Calendar.SECOND)
    val isPm = hour24 >= 12
    val h12 = hour24 % 12
    val hour12 = if (h12 == 0) 12 else h12

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day)

    val dowIndex = (cal.get(Calendar.DAY_OF_WEEK) - 1).coerceIn(0, 6)

    return ClockTime(
        hour24 = hour24,
        minute = minute,
        second = second,
        isPm = isPm,
        hour12 = hour12,
        dateStr = dateStr,
        dayOfWeek = DAYS_OF_WEEK[dowIndex],
        colonLit = second % 2 == 0,
        zoneLabel = zoneLabel(zone)
    )
}

/** Resolve a zone id to a [TimeZone], falling back to the device default. */
fun safeZone(zoneId: String): TimeZone {
    val zone = TimeZone.getTimeZone(zoneId)
    // Unknown ids resolve to "GMT"; treat that mismatch (unless literally GMT) as default.
    return if (zone.id == "GMT" && zoneId != "GMT") TimeZone.getDefault() else zone
}

/** A short human-readable label for a zone, e.g. "GMT+08:00 Shanghai". */
fun zoneLabel(zone: TimeZone): String {
    val offsetMs = zone.getOffset(System.currentTimeMillis())
    val totalMinutes = offsetMs / 60000
    val sign = if (totalMinutes < 0) "-" else "+"
    val absMinutes = kotlin.math.abs(totalMinutes)
    val hh = absMinutes / 60
    val mm = absMinutes % 60
    val gmt = String.format(Locale.getDefault(), "GMT%s%02d:%02d", sign, hh, mm)
    val city = zone.id.substringAfterLast('/', zone.id).replace('_', ' ')
    return "$gmt $city"
}
