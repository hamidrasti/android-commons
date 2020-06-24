package com.github.hamraa.androidcommons.time

import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.abs

object Time {

    private val TIMES = mapOf(
        "year" to "سال",
        "month" to "ماه",
        "week" to "هفته",
        "day" to "روز",
        "hour" to "ساعت",
        "minute" to "دقیقه",
        "second" to "ثانیه"
    )

    private val ZONE_TEHRAN = ZoneId.of("Asia/Tehran")
    private val DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.of("UTC"))

    fun period(
        date: String?,
        short: Boolean = true,
        defaultString: String = "همین الان",
        formatter: DateTimeFormatter = DEFAULT_FORMATTER
    ): String {

        if (date.isNullOrBlank()) return defaultString

        val dateTime = LocalDateTime.parse(date, formatter)
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val period = Period.between(dateTime.toLocalDate(), now.toLocalDate())
        val duration: Duration = Duration.between(dateTime, now)
        if (duration.isZero) return defaultString
        val suffix = if (duration.isNegative || period.isNegative) "بعد" else "پیش"
        val builder = StringBuilder()
        var parts = 0
        if (period.years != 0) {
            builder.append("${abs(period.years)} ${TIMES["year"]}")
            if (short) return "$builder $suffix"
            parts++
        }
        if (period.months != 0) {
            if (parts > 0) builder.append(", ")
            builder.append("${abs(period.months)} ${TIMES["month"]}")
            if (short) return "$builder $suffix"
            parts++
        }
        if (period.days != 0) {
            if (parts > 0) builder.append(", ")
            builder.append("${abs(period.days)} ${TIMES["day"]}")
            if (short) return "$builder $suffix"
            parts++
        }
        if (duration.toHours() != 0L) {
            if (parts > 0) builder.append(", ")
            builder.append("${abs(duration.toHours())} ${TIMES["hour"]}")
            if (short) return "$builder $suffix"
            parts++
        }
        if (duration.toMinutes() != 0L) {
            if (parts > 0) builder.append(", ")
            builder.append("${abs(duration.toMinutes())} ${TIMES["minute"]}")
            if (short) return "$builder $suffix"
            parts++
        }
        if (duration.seconds != 0L) {
            if (parts > 0) builder.append(", ")
            builder.append("${abs(duration.seconds)} ${TIMES["second"]}")
            if (short) return "$builder $suffix"
            parts++
        }
        if (parts == 0) return defaultString
        return "$builder $suffix"
    }

    fun clock(date: String?, formatter: DateTimeFormatter = DEFAULT_FORMATTER): String {
        if (date.isNullOrBlank()) return ""
        val dateTime = ZonedDateTime.parse(date, formatter).withZoneSameInstant(ZONE_TEHRAN)
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun toPersian(
        date: String?,
        withTime: Boolean = false,
        // readable: Boolean = false,
        formatter: DateTimeFormatter = DEFAULT_FORMATTER
    ): String {
        if (date.isNullOrBlank()) return ""
        val dateTime = ZonedDateTime.parse(date, formatter).withZoneSameInstant(ZONE_TEHRAN)
        return try {
            if (withTime) "${PersianDate.fromGregorian(dateTime.toLocalDate())} - ${dateTime.toLocalTime()}"
            else PersianDate.fromGregorian(dateTime.toLocalDate()).toString()
        } catch (ignored: Exception) {
            "unknown"
        }
        // return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    }
}