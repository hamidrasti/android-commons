@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.github.hamraa.androidcommons.time

import com.github.hamraa.androidcommons.time.extensions.floorDiv
import com.github.hamraa.androidcommons.time.extensions.floorMod
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoPeriod
import java.time.chrono.Chronology
import java.time.temporal.*
import java.util.*
import kotlin.math.ceil

/**
 * This is an implementation of Solar Hijri calendar (also known as Jalali calendar,
 * Persian calendar).
 *
 *
 * `PersianDate` is an immutable date-time object that represents a date,
 * often viewed as year-month-day.
 *
 *
 * In order to simplify usage of this class, it is tried to make API of this class
 * the same as JDK8 [LocalDate] class. Since some methods of [LocalDate] were
 * useful for Persian calendar system, they have been exactly copied. Some other methods of
 * [java.time.chrono.HijrahDate] and [java.time.chrono.JapaneseDate] have been
 * modified and used in this class.
 *
 * Constructor.
 *
 * @param year       the year to represent, from 1 to MAX_YEAR
 * @param month      the month-of-year to represent, not null, from [PersianMonth] enum
 * @param dayOfMonth the dayOfMonth-of-month to represent, from 1 to 31
 * @throws DateTimeException if the passed parameters do not form a valid date or time.
 *
 */
class PersianDate private constructor(year: Int, month: Int, dayOfMonth: Int) : ChronoLocalDate {

    /**
     * @return the year
     */
    /**
     * The year.
     */
    val year: Int

    /**
     * @return the month-of-year, from 1 to 12
     * @see .getMonth
     */
    /**
     * The month-of-year.
     */
    val monthValue: Int

    /**
     * @return day-of-month, from 1 to 31
     */
    /**
     * The day-of-month.
     */
    val dayOfMonth: Int

    init {
        PersianChronology.INSTANCE.checkValidValue(year.toLong(), ChronoField.YEAR)
        PersianChronology.INSTANCE.checkValidValue(
            month.toLong(),
            ChronoField.MONTH_OF_YEAR
        )
        val leapYear: Boolean = PersianChronology.INSTANCE.isLeapYear(year.toLong())
        val maxDaysOfMonth: Int = PersianMonth.of(month).length(leapYear)
        if (dayOfMonth > maxDaysOfMonth) {
            if (month == 12 && dayOfMonth == 30 && !leapYear) {
                throw DateTimeException("Invalid date ESFAND 30, as $year is not a leap year")
            }
            throw DateTimeException("Invalid date " + PersianMonth.of(month).name + " " + dayOfMonth)
        }
        this.year = year
        monthValue = month
        this.dayOfMonth = dayOfMonth
    }

    /**
     * @return the month-of-year field using the `Month` enum.
     * @see .getMonthValue
     */
    fun getMonth(): PersianMonth {
        return PersianMonth.of(monthValue)
    }

    /**
     * @return day-of-year, from 1 to 365 or 366 in a leap year
     */
    val dayOfYear: Int
        get() = PersianMonth.of(monthValue).daysToFirstOfMonth() + dayOfMonth

    /**
     * Returns day-of-week as an enum [DayOfWeek]. This avoids confusion as to what
     * `int` means. If you need access to the primitive `int` value then the
     * enum provides the [int value][DayOfWeek.getValue].
     *
     * @return day-of-week, which is an enum [DayOfWeek]
     */
    val dayOfWeek: DayOfWeek
        get() = DayOfWeek.of(((toJulianDay() + 1) % 7 + 1).toInt())
    //-----------------------------------------------------------------------
    /**
     * Gets the chronology of this date, which is the Persian calendar system.
     *
     *
     * The `Chronology` represents the calendar system in use.
     * The era and other fields in [ChronoField] are defined by the chronology.
     *
     * @return the Persian chronology, not null
     */
    override fun getChronology(): Chronology {
        return PersianChronology.INSTANCE
    }

    /**
     * Returns the length of the month represented by this date.
     *
     *
     * This returns the length of the month in days.
     *
     * @return the length of the month in days
     */
    override fun lengthOfMonth(): Int {
        val pm: PersianMonth = PersianMonth.of(monthValue)
        return if (PersianChronology.INSTANCE.isLeapYear(year.toLong())) pm.maxLength() else pm.minLength()
    }

    /**
     * Calculates the amount of time until another date in terms of the specified unit.
     *
     *
     * This calculates the amount of time between two `PersianDate`
     * objects in terms of a single `TemporalUnit`.
     * The start and end points are `this` and the specified date.
     * The result will be negative if the end is before the start.
     * The `Temporal` passed to this method is converted to a
     * `PersianDate` using [.from].
     * For example, the amount in days between two dates can be calculated
     * using `startDate.until(endDate, DAYS)`.
     *
     *
     * The calculation returns a whole number, representing the number of
     * complete units between the two dates.
     * For example, the amount in months between 1396-06-15 and 1396-08-14
     * will only be one month as it is one day short of two months.
     *
     *
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use [TemporalUnit.between]:
     * <pre>
     * // these two lines are equivalent
     * amount = start.until(end, MONTHS);
     * amount = MONTHS.between(start, end);
    </pre> *
     * The choice should be made based on which makes the code more readable.
     *
     *
     * The calculation is implemented in this method for [ChronoUnit].
     * The units `DAYS`, `WEEKS`, `MONTHS`, `YEARS`,
     * `DECADES`, `CENTURIES`, `MILLENNIA` and `ERAS`
     * are supported. Other `ChronoUnit` values will throw an exception.
     *
     *
     * If the unit is not a `ChronoUnit`, then the result of this method
     * is obtained by invoking `TemporalUnit.between(Temporal, Temporal)`
     * passing `this` as the first argument and the converted input temporal
     * as the second argument.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param endExclusive the end date, exclusive, which is converted to a `PersianDate`, not null
     * @param unit         the unit to measure the amount in, not null
     * @return the amount of time between this date and the end date
     * @throws DateTimeException                if the amount cannot be calculated, or the end
     * temporal cannot be converted to a `PersianDate`
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    override fun until(
        endExclusive: Temporal,
        unit: TemporalUnit
    ): Long {
        Objects.requireNonNull(endExclusive, "endExclusive")
        Objects.requireNonNull(unit, "unit")
        val end = chronology.date(endExclusive) as PersianDate
        if (unit is ChronoUnit) {
            when (unit) {
                ChronoUnit.DAYS -> return daysUntil(end)
                ChronoUnit.WEEKS -> return daysUntil(end) / 7
                ChronoUnit.MONTHS -> return monthsUntil(end)
                ChronoUnit.YEARS -> return monthsUntil(end) / 12
                ChronoUnit.DECADES -> return monthsUntil(end) / 120
                ChronoUnit.CENTURIES -> return monthsUntil(end) / 1200
                ChronoUnit.MILLENNIA -> return monthsUntil(end) / 12000
                ChronoUnit.ERAS -> return end.getLong(ChronoField.ERA) - getLong(
                    ChronoField.ERA
                )
            }
            throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
        }
        return unit.between(this, end)
    }

    private fun daysUntil(end: PersianDate): Long {
        return end.toEpochDay() - toEpochDay() // no overflow
    }

    private fun monthsUntil(end: PersianDate): Long {
        val packed1 = getLong(ChronoField.PROLEPTIC_MONTH) * 32L + dayOfMonth // no overflow
        val packed2 = end.getLong(ChronoField.PROLEPTIC_MONTH) * 32L + end.dayOfMonth // no overflow
        return (packed2 - packed1) / 32
    }

    /**
     * Calculates the period between this date and another date as a `Period`.
     *
     *
     * This calculates the period between two dates in terms of years, months and days.
     * The start and end points are `this` and the specified date.
     * The result will be negative if the end is before the start.
     * The negative sign will be the same in each of year, month and day.
     *
     *
     * The calculation is performed using the ISO calendar system.
     * If necessary, the input date will be converted to ISO.
     *
     *
     * The start date is included, but the end date is not.
     * The period is calculated by removing complete months, then calculating
     * the remaining number of days, adjusting to ensure that both have the same sign.
     * The number of months is then normalized into years and months based on a 12 month year.
     * A month is considered to be complete if the end day-of-month is greater
     * than or equal to the start day-of-month.
     * For example, from `2010-01-15` to `2011-03-18` is "1 year, 2 months and 3 days".
     *
     *
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use [Period.between]:
     * <pre>
     * // these two lines are equivalent
     * period = start.until(end);
     * period = Period.between(start, end);
    </pre> *
     * The choice should be made based on which makes the code more readable.
     *
     * @param endDateExclusive the end date, exclusive, which may be in any chronology, not null
     * @return the period between this date and the end date, not null
     */
    override fun until(endDateExclusive: ChronoLocalDate): ChronoPeriod {
        Objects.requireNonNull(endDateExclusive, "endDateExclusive")
        val end: PersianDate = PersianChronology.INSTANCE.date(endDateExclusive)
        var totalMonths =
            end.getLong(ChronoField.PROLEPTIC_MONTH) - getLong(ChronoField.PROLEPTIC_MONTH) // safe
        var days = end.dayOfMonth - dayOfMonth
        if (totalMonths > 0 && days < 0) {
            totalMonths--
            val calcDate = plusMonths(totalMonths)
            days = (end.toEpochDay() - calcDate.toEpochDay()).toInt() // safe
        } else if (totalMonths < 0 && days > 0) {
            totalMonths++
            days -= end.lengthOfMonth()
        }
        val years = totalMonths / 12 // safe
        val months = (totalMonths % 12).toInt() // safe
        return Period.of(Math.toIntExact(years), months, days)
    }

    /**
     * Gets the value of the specified field from this date as a `long`.
     *
     *
     * This queries this date for the value for the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The [supported fields][.isSupported] will return valid
     * values based on this date.
     * All other `ChronoField` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.getFrom(TemporalAccessor)`
     * passing `this` as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field the field to get, not null
     * @return the value for the field
     * @throws DateTimeException                if a value for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    override fun getLong(field: TemporalField): Long {
        if (field is ChronoField) {
            when (field) {
                ChronoField.DAY_OF_WEEK -> return dayOfWeek.value.toLong()
                ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH -> return ((dayOfMonth - 1) % 7 + 1).toLong()
                ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR -> return ((dayOfYear - 1) % 7 + 1).toLong()
                ChronoField.DAY_OF_MONTH -> return dayOfMonth.toLong()
                ChronoField.DAY_OF_YEAR -> return dayOfYear.toLong()
                ChronoField.EPOCH_DAY -> return toEpochDay()
                ChronoField.ALIGNED_WEEK_OF_MONTH -> return ((dayOfMonth - 1) / 7 + 1).toLong()
                ChronoField.ALIGNED_WEEK_OF_YEAR -> return ((dayOfYear - 1) / 7 + 1).toLong()
                ChronoField.MONTH_OF_YEAR -> return monthValue.toLong()
                ChronoField.PROLEPTIC_MONTH -> return year * 12L + monthValue - 1
                ChronoField.YEAR_OF_ERA -> return if (year >= 1) year.toLong() else (1 - year).toLong()
                ChronoField.YEAR -> return year.toLong()
                ChronoField.ERA -> return if (year >= 1) 1 else 0
            }
        }
        throw UnsupportedTemporalTypeException("Unsupported field: $field")
    }

    /**
     * Returns a copy of this `PersianDate` with the specified period in years added.
     *
     *
     * This method adds the specified amount to the years field in three steps:
     *
     *  1. Add the input years to the year field
     *  1. Check if the resulting date would be invalid
     *  1. Adjust the day-of-month to the last valid day if necessary
     *
     *
     *
     * For example, 1387-12-30 (leap year) plus one year would result in the
     * invalid date 1388-12-30 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 1388-12-29, is selected instead.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToAdd the years to add, may be negative
     * @return a `PersianDate` based on this date with the years added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusYears(yearsToAdd: Long): PersianDate {
        return plusMonths(yearsToAdd * 12)
    }

    /**
     * Returns a copy of this `PersianDate` with the specified period in months added.
     *
     *
     * This method adds the specified amount to the months field in three steps:
     *
     *  1. Add the input months to the month-of-year field
     *  1. Check if the resulting date would be invalid
     *  1. Adjust the day-of-month to the last valid day if necessary
     *
     *
     *
     * For example, 1388-11-30 plus one month would result in the invalid date
     * 1388-12-30. Instead of returning an invalid result, the last valid day
     * of the month, 1388-12-29, is selected instead.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToAdd the months to add, may be negative
     * @return a `PersianDate` based on this date with the months added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusMonths(monthsToAdd: Long): PersianDate {
        if (monthsToAdd == 0L) {
            return this
        }
        val monthCount = year * 12L + (monthValue - 1)
        val calcMonths = monthCount + monthsToAdd
        val newYear = calcMonths.floorDiv(12L).toInt()
        val newMonth = calcMonths.floorMod(12L).toInt() + 1
        return resolvePreviousValid(newYear, newMonth, dayOfMonth)
    }

    /**
     * Returns a copy of this `PersianDate` with the specified number of days added.
     *
     *
     * This method adds the specified amount to the days field incrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     *
     *
     * For example, 1396-12-29 plus one day would result in 1397-01-01.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param daysToAdd the days to add, may be negative
     * @return a `PersianDate` based on this date with the days added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    fun plusDays(daysToAdd: Long): PersianDate {
        return if (daysToAdd == 0L) this
        else ofJulianDays(toJulianDay() + daysToAdd)
    }

    /**
     * Returns true if `year` is a leap year in Persian calendar.
     *
     * @return true if `year` is a leap year in Persian calendar
     */
    override fun isLeapYear(): Boolean {
        return PersianChronology.INSTANCE.isLeapYear(year.toLong())
    }

    /**
     * Resolves the date, resolving days past the end of month.
     *
     * @param year  the year to represent
     * @param month the month-of-year to represent, validated from 1 to 12
     * @param day   the day-of-month to represent, validated from 1 to 31
     * @return the resolved date, not null
     */
    private fun resolvePreviousValid(year: Int, month: Int, day: Int): PersianDate {
        var dayOfMonth = day
        val leapYear: Boolean = PersianChronology.INSTANCE.isLeapYear(year.toLong())
        val maxDaysOfMonth: Int = PersianMonth.of(month).length(leapYear)
        if (dayOfMonth > maxDaysOfMonth) {
            dayOfMonth = maxDaysOfMonth
        }
        return of(year, month, dayOfMonth)
    }

    /**
     * Returns an equivalent Gregorian date and time as an instance of [LocalDate].
     * Calling this method has no effect on the object that calls this.
     *
     * @return the equivalent Gregorian date as an instance of [LocalDate]
     */
    fun toGregorian(): LocalDate {
        return LocalDate.from(this)
    }

    override fun toEpochDay(): Long {
        return toJulianDay() - JULIAN_DAY_TO_1970
    }

    /**
     * Returns number of corresponding julian days. For number of juliand days of
     * PersianDate.of(1396, 8, 6) is 2458054.
     *
     *
     * Calling this method has no effect on this instance.
     *
     * @return number of corresponding julian days
     * @see [calendar convertor](http://www.fourmilab.ch/documents/calendar/)
     */
    fun toJulianDay(): Long {
        return toJulianDay(year, monthValue, dayOfMonth)
    }
    //-----------------------------------------------------------------------
    /**
     * Checks if this date is equal to another date.
     *
     *
     * Compares this `PersianDate` with another ensuring that the date is the same.
     *
     * @param other the object to check, null returns false
     * @return true if this is equal to the other date
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is PersianDate) compareTo(other as PersianDate?) == 0 else false
    }

    /**
     * A hash code for this persian date.
     *
     * @return a suitable hash code
     */
    override fun hashCode(): Int {
        return Objects.hash(year, monthValue, dayOfMonth)
    }
    //-----------------------------------------------------------------------
    /**
     * Returns the string representation of this persian date. The string contains of ten
     * characters whose format is "XXXX-YY-ZZ", where XXXX is the year, YY is the
     * month-of-year and ZZ is day-of-month. (Each of the capital characters represents a
     * single decimal digit.)
     *
     *
     * If any of the three parts of this persian date is too small to fill up its field,
     * the field is padded with leading zeros.
     *
     * @return a suitable representation of this persian date
     */
    override fun toString(): String {
        return String.format("%04d/%02d/%02d", year, monthValue, dayOfMonth)
    }

    companion object {

        /**
         * The minimum supported persian date `0001-01-01`.
         */
        val MIN = of(PersianChronology.INSTANCE.range(ChronoField.YEAR).minimum.toInt(), 1, 1)

        /**
         * The maximum supported persian date `1999-12-29`.
         */
        val MAX = of(PersianChronology.INSTANCE.range(ChronoField.YEAR).maximum.toInt(), 12, 29)

        /**
         * 1970-01-01 to julidan day.
         */
        private const val JULIAN_DAY_TO_1970 = 2440587L

        /**
         * Obtains current Persian date from the system clock in the default time zone.
         *
         * @return current Persian date from the system clock in the default time zone
         */
        fun now(): PersianDate {
            return ofEpochDay(LocalDate.now().toEpochDay())
        }

        /**
         * Obtains an instance of `PersianDate` with year, month and day of month.
         * The value of month must be between `1` and `12`. Value `1` would
         * be [PersianMonth.FARVARDIN] and value `12` represents
         * [PersianMonth.ESFAND].
         *
         * @param year       the year to represent, from 1 to MAX_YEAR
         * @param month      the value of month, from 1 to 12
         * @param dayOfMonth the dayOfMonth to represent, from 1 to 31
         * @return an instance of `PersianDate`
         * @throws DateTimeException if the passed parameters do not form a valid date or time.
         */
        fun of(year: Int, month: Int, dayOfMonth: Int): PersianDate {
            return PersianDate(year, month, dayOfMonth)
        }

        /**
         * Obtains an instance of `PersianDate` with year, month and day of month.
         *
         * @param year       the year to represent, from 1 to MAX_YEAR
         * @param month      the month-of-year to represent, an instance of [PersianMonth]
         * @param dayOfMonth the dayOfMonth to represent, from 1 to 31
         * @return an instance of `PersianDate`
         * @throws DateTimeException if the passed parameters do not form a valid date or time.
         */
        fun of(year: Int, month: PersianMonth, dayOfMonth: Int): PersianDate {
            Objects.requireNonNull(month, "month")
            return PersianDate(year, month.value, dayOfMonth)
        }

        /**
         * Returns an instance of `PersianDate` that is correspondent to the gregorian
         * date of parameter `localDate`.
         *
         * @param localDate Gregorian date and time, not null
         * @return an equivalent Persian date and time as an instance of [PersianDate]
         */
        fun fromGregorian(localDate: LocalDate): PersianDate {
            Objects.requireNonNull(localDate, "localDate")
            return ofEpochDay(localDate.toEpochDay())
        }

        /**
         * Returns an instance of [PersianDate], based on number of epoch days,
         * which is from 1970-01-01. For example passing `17468` as the parameter
         * results a persian date of 1396-08-07.
         *
         * @param epochDays epoch days
         * @return an instance of [PersianDate]
         */
        fun ofEpochDay(epochDays: Long): PersianDate {
            return ofJulianDays(epochDays + JULIAN_DAY_TO_1970)
        }

        /**
         * Returns an instance of [PersianDate], based on number of julian days.
         * For example passing `2458054` as the parameter will cause to get a
         * Persian date of "1396-8-6".
         *
         * @param julianDays julian days
         * @return an instance of [PersianDate]
         * @see [calendar convertor](http://www.fourmilab.ch/documents/calendar/)
         */
        fun ofJulianDays(julianDays: Long): PersianDate {
            Utils.longRequirePositive(julianDays, "julianDays")
            val depoch = julianDays - 2121445L
            val cycle = depoch / 1029983L
            val cyear = depoch % 1029983L
            var ycycle: Long
            val aux1: Long
            val aux2: Long
            if (cyear == 1029982L) {
                ycycle = 2820L
            } else {
                aux1 = cyear / 366L
                aux2 = cyear % 366L
                ycycle = (2134L * aux1 + 2816L * aux2 + 2815L) / 1028522L + aux1
                ycycle = if (ycycle >= 0) ycycle + 1L else ycycle
            }
            // Check year '474'
            ycycle = if (!Utils.isBetween(julianDays, 2121079, 2121444)) ycycle else 0
            val pYear = ycycle + 2820L * cycle + 474L
            val yday = (julianDays - of(pYear.toInt(), 1, 1).toJulianDay() + 1).toInt()
            val pMonth = ceil(if (yday <= 186) yday / 31.0 else (yday - 6) / 30.0).toInt()
            val pDay = (julianDays - of(pYear.toInt(), pMonth, 1).toJulianDay() + 1).toInt()
            return of(pYear.toInt(), pMonth, pDay)
        }

        /**
         * Returns number of corresponding julian days. For number of juliand days of
         * PersianDate.of(1396, 8, 6) is 2458054. This method is provided in order to
         * prevent creating unnecessary instances of `PersianDate` only to calculate
         * julian day.
         *
         * @return number of corresponding julian days
         * @see [calendar convertor](http://www.fourmilab.ch/documents/calendar/)
         */
        fun toJulianDay(year: Int, month: Int, dayOfMonth: Int): Long {
            val epbase = year - 474
            val epyear = 474 + epbase % 2820
            return (dayOfMonth + PersianMonth.of(month)
                .daysToFirstOfMonth() + (epyear * 682 - 110) / 2816 + (epyear - 1) * 365 +
                    epbase / 2820 * 1029983 +
                    (1948320 - 1)).toLong()
        }
    }
}