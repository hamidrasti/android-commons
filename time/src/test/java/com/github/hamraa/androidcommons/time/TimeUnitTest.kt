package com.github.hamraa.androidcommons.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimeUnitTest {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.of("UTC"))

    @Test
    fun period_before() {
        val now = LocalDateTime.now(ZoneOffset.UTC).minusSeconds(1).format(formatter)
        assertEquals("1 ثانیه پیش", Time.period(now))
    }

    @Test
    fun period_equal() {
        val now = LocalDateTime.now(ZoneOffset.UTC).format(formatter)
        assertEquals("همین الان", Time.period(now))

        val nowMinusNanos = LocalDateTime.now(ZoneOffset.UTC).minusNanos(9999999).format(formatter)
        assertEquals("همین الان", Time.period(nowMinusNanos))

        val nowPlusNanos = LocalDateTime.now(ZoneOffset.UTC).plusNanos(9999999).format(formatter)
        assertEquals("همین الان", Time.period(nowPlusNanos))
    }

    @Test
    fun period_after() {
        val now = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(1).format(formatter)
        assertEquals("1 ثانیه بعد", Time.period(now))
    }
}