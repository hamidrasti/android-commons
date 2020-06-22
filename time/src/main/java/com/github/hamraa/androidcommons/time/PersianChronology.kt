@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.hamraa.androidcommons.time

import java.time.DateTimeException
import java.time.chrono.AbstractChronology
import java.time.chrono.ChronoLocalDate
import java.time.chrono.Era
import java.time.temporal.*
import java.util.*

/**
 * The Persian calendar (also known as Jalali calendar or Iranian calendar) is a solar calendar.
 * PersianChronology follows the rules of Persian Calendar.
 *
 *
 * Length of each month is between 29 to 31 days. Details of each month is implemented in
 * [PersianMonth] enum.
 *
 *
 * Normal yeras have 365 days and leap years have 366 days.
 */
class PersianChronology private constructor() : AbstractChronology() {

    /**
     * Checks whther parameter `value` is valid or not. If `value` is out
     * of range, an DateTimeException will be thrown with a suitable message.
     *
     * @param value value to check
     */
    fun checkValidValue(value: Long, field: TemporalField) {
        Objects.requireNonNull(field, "field")
        if (field !is ChronoField) {
            throw DateTimeException("Parameter 'field' is not supported")
        }
        if (!Utils.isBetween(value, range(field).minimum, range(field).maximum)) {
            throw DateTimeException("Invalid value for " + field + ", valid values: " + range(field))
        }
    }

    /**
     * Checks whether parameter `dayOfYear` is valid in year `year`. If it is
     * not valid, an IllegalArgumentException will be thrown, otherwise `dayOfYear`
     * is returned.
     *
     * @param year      year that `dayOfYear` to be checked in, valid range is from
     * minimum year to maximum year
     * @param dayOfYear the day-of-year to be checked, from 1 to 365 or 366 in a leap year
     */
    fun checkDayOfYear(year: Int, dayOfYear: Int) {
        checkValidValue(year.toLong(), ChronoField.YEAR)
        val maxDayOfYear = if (isLeapYear(year.toLong())) 366 else 365
        if (!Utils.isBetween(dayOfYear, 1, maxDayOfYear)) {
            throw DateTimeException("Invalid value for dayOfYear: $dayOfYear ")
        }
    }
    //-----------------------------------------------------------------------
    /**
     * Gets the ID of the chronology.
     *
     *
     * The ID uniquely identifies the `Chronology`. It can be used to
     * lookup the `Chronology` using [.of].
     *
     * @return the chronology ID, non-null
     * @see .getCalendarType
     */
    override fun getId(): String {
        return "Persian"
    }

    /**
     * Gets the calendar type of the Islamic calendar.
     *
     *
     * The calendar type is an identifier defined by the
     * *Unicode Locale Data Markup Language (LDML)* specification.
     * It can be used to lookup the `Chronology` using [.of].
     *
     * @return the calendar system type; non-null if the calendar has
     * a standard type, otherwise null
     * @see .getId
     */
    override fun getCalendarType(): String {
        return "persian"
    }

    /**
     * Obtains a local date in this chronology from the proleptic-year,
     * month-of-year and day-of-month fields.
     *
     * @param prolepticYear the chronology proleptic-year
     * @param month         the chronology month-of-year
     * @param dayOfMonth    the chronology day-of-month
     * @return the local date in this chronology, not null
     * @throws DateTimeException if unable to create the date
     */
    override fun date(prolepticYear: Int, month: Int, dayOfMonth: Int): PersianDate {
        return PersianDate.of(prolepticYear, month, dayOfMonth)
    }

    /**
     * Obtains a local date in this chronology from the proleptic-year and
     * day-of-year fields.
     *
     * @param prolepticYear the chronology proleptic-year
     * @param dayOfYear     the chronology day-of-year
     * @return the local date in this chronology, not null
     * @throws DateTimeException if unable to create the date
     */
    override fun dateYearDay(prolepticYear: Int, dayOfYear: Int): PersianDate {
        checkDayOfYear(prolepticYear, dayOfYear)
        return PersianDate.of(prolepticYear, 1, 1).plusDays(dayOfYear - 1.toLong())
    }

    /**
     * Obtains a local date in this chronology from the epoch-day.
     *
     *
     * The definition of [EPOCH_DAY][ChronoField.EPOCH_DAY] is the same
     * for all calendar systems, thus it can be used for conversion.
     *
     * @param epochDay the epoch day
     * @return the local date in this chronology, not null
     * @throws DateTimeException if unable to create the date
     */
    override fun dateEpochDay(epochDay: Long): PersianDate {
        return PersianDate.ofEpochDay(epochDay)
    }

    /**
     * Obtains a local date in this chronology from another temporal object.
     *
     *
     * This obtains a date in this chronology based on the specified temporal.
     * A `TemporalAccessor` represents an arbitrary set of date and time information,
     * which this factory converts to an instance of `ChronoLocalDate`.
     *
     *
     * The conversion typically uses the [EPOCH_DAY][ChronoField.EPOCH_DAY]
     * field, which is standardized across calendar systems.
     *
     *
     * This method matches the signature of the functional interface [TemporalQuery]
     * allowing it to be used as a query via method reference, `aChronology::date`.
     *
     * @param temporal the temporal object to convert, not null
     * @return the local date in this chronology, not null
     * @throws DateTimeException if unable to create the date
     * @see ChronoLocalDate.from
     */
    override fun date(temporal: TemporalAccessor): PersianDate {
        return if (temporal is PersianDate) temporal
        else PersianDate.ofEpochDay(temporal.getLong(ChronoField.EPOCH_DAY))
    }

    /**
     * Returns true if `year` is a leap year in Persian calendar.
     *
     * @param year the year to be checked whether is a leap year or not. For valid
     * range, check [.range].
     * @return true if `year` is a leap year in Persian calendar
     */
    override fun isLeapYear(year: Long): Boolean {
        checkValidValue(year, ChronoField.YEAR)
        return PersianDate.toJulianDay((year + 1).toInt(), 1, 1) -
                PersianDate.toJulianDay(year.toInt(), 1, 1) > 365
    }

    /**
     * Calculates the proleptic-year given the era and year-of-era.
     *
     *
     * This combines the era and year-of-era into the single proleptic-year field.
     *
     * @param era       the era of the correct type for the chronology, not null
     * @param yearOfEra the chronology year-of-era
     * @return the proleptic-year
     * @throws DateTimeException  if unable to convert to a proleptic-year,
     * such as if the year is invalid for the era
     * @throws ClassCastException if the `era` is not of the correct type for the chronology
     */
    override fun prolepticYear(era: Era, yearOfEra: Int): Int {
        if (era !is PersianEra) throw ClassCastException("Era must be PersianEra")
        return yearOfEra
    }

    /**
     * Creates the chronology era object from the numeric value.
     *
     *
     * The era is, conceptually, the largest division of the time-line.
     * Most calendar systems have a single epoch dividing the time-line into two eras.
     * However, some have multiple eras, such as one for the reign of each leader.
     * The exact meaning is determined by the chronology according to the following constraints.
     *
     *
     * This method returns the singleton era of the correct type for the specified era value.
     *
     * @param eraValue the era value
     * @return the calendar system era, not null
     * @throws DateTimeException if unable to create the era
     */
    override fun eraOf(eraValue: Int): Era {
        if (eraValue == 1) return PersianEra.AHS
        throw DateTimeException("invalid Persian era")
    }

    /**
     * Gets the list of eras for the chronology.
     *
     * @return the list of eras for the chronology, may be immutable, not null
     */
    override fun eras(): List<Era> {
        return listOf<Era>(*PersianEra.values())
    }

    /**
     * Gets the range of valid values for the specified field.
     *
     *
     * All fields can be expressed as a `long` integer.
     * This method returns an object that describes the valid range for that value.
     *
     *
     * Note that the result only describes the minimum and maximum valid values
     * and it is important not to read too much into them. For example, there
     * could be values within the range that are invalid for the field.
     *
     *
     * This method will return a result whether or not the chronology supports the field.
     *
     * @param field the field to get the range for, not null
     * @return the range of valid values for the field, not null
     * @throws DateTimeException if the range for the field cannot be obtained
     */
    override fun range(field: ChronoField): ValueRange {
        return when (field) {
            ChronoField.DAY_OF_MONTH -> ValueRange.of(1, 1, 29, 31)
            ChronoField.DAY_OF_YEAR -> ValueRange.of(1, 1, 365, 366)
            ChronoField.ALIGNED_WEEK_OF_MONTH -> ValueRange.of(1, 5)
            ChronoField.YEAR, ChronoField.YEAR_OF_ERA -> ValueRange.of(1, 1999)
            ChronoField.ERA -> ValueRange.of(1, 1)
            else -> field.range()
        }
    }

    companion object {
        /**
         * Single instance of this class.
         */
        val INSTANCE = PersianChronology()
    }
}