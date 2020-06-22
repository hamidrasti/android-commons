@file:Suppress("unused")

package com.github.hamraa.androidcommons.time

/**
 * A month-of-year, such as Mehr.
 *
 *
 * `PersianMonth` is an enum representing the 12 months of the year
 * Farvardin, Ordibehesht, Khordad, Tir, Mordad, Shahrivar, Mehr, Aban, Azar, Dey,
 * Bahman and Esfand.
 *
 *
 * In addition to the textual enum name, each month-of-year has an `int` value.
 * The `int` value follows normal usage and the ISO-8601 standard,
 * from 1 (January) to 12 (December). It is recommended that applications use the enum
 * rather than the `int` value to ensure code clarity.
 *
 *
 * **Do not use `ordinal()` to obtain the numeric representation of `Month`.
 * Use `getValue()` instead.**
 */
enum class PersianMonth(
    /**
     * Returns name of month in persian alphabe.
     *
     * @return persian name of month.
     */
    val persianName: String
) {
    /**
     * The singleton instance for the month of Farvardin with 31 days.
     * This has the numeric value of `1`.
     */
    FARVARDIN("فروردین"),

    /**
     * The singleton instance for the month of Ordibehesht with 31 days.
     * This has the numeric value of `2`.
     */
    ORDIBEHESHT("اردیبهشت"),

    /**
     * The singleton instance for the month of Khordad with 31 days.
     * This has the numeric value of `3`.
     */
    KHORDAD("خرداد"),

    /**
     * The singleton instance for the month of Tir with 31 days.
     * This has the numeric value of `4`.
     */
    TIR("تیر"),

    /**
     * The singleton instance for the month of Mordad with 31 days.
     * This has the numeric value of `5`.
     */
    MORDAD("مرداد"),

    /**
     * The singleton instance for the month of Shahrivar with 31 days.
     * This has the numeric value of `6`.
     */
    SHAHRIVAR("شهریور"),

    /**
     * The singleton instance for the month of Mehr with 30 days.
     * This has the numeric value of `7`.
     */
    MEHR("مهر"),

    /**
     * The singleton instance for the month of Aban with 30 days.
     * This has the numeric value of `8`.
     */
    ABAN("آبان"),

    /**
     * The singleton instance for the month of Azar with 30 days.
     * This has the numeric value of `9`.
     */
    AZAR("آذر"),

    /**
     * The singleton instance for the month of Dey with 30 days.
     * This has the numeric value of `10`.
     */
    DEY("دی"),

    /**
     * The singleton instance for the month of Bahman with 30 days.
     * This has the numeric value of `11`.
     */
    BAHMAN("بهمن"),

    /**
     * The singleton instance for the month of Farvardin with 29 days in non-leap year
     * and 30 days in leap year. This has the numeric value of `12`.
     */
    ESFAND("اسفند");

    /**
     * @return number of month, from 1 (Farvardin) to 12 (Esfand)
     */
    val value: Int
        get() = ordinal + 1

    /**
     * Returns length of month. For the first six months of year, `31` is returned.
     * For the second six months of year except Esfand, `30` is returned.
     * Finlally For Esfand, if `leapYear` is true, it returns `30`, otherwise
     * it returns `29`.
     *
     * @param leapYear true, if length of months in leap year is required
     * @return length of months of Persian calendar
     */
    fun length(leapYear: Boolean): Int {
        val value = value
        return if (value < 7) 31 else if (value != 12) 30 else if (leapYear) 30 else 29
    }

    /**
     * Returns length of month in a leap year. For the first six months of year,
     * `31` is returned. For the second six months of year `30` is returned.
     *
     * @return length of months of Persian calendar
     */
    fun maxLength(): Int {
        return length(true)
    }

    /**
     * Returns length of month in a non-leap year. For the first six months of year,
     * `31` is returned. For the second six months of year except Esfand, `30`
     * is returned. For Esfand `29` is returned.
     *
     * @return length of months of Persian calendar
     */
    fun minLength(): Int {
        return length(false)
    }

    /**
     * Returns a month-of-year that is `months` after current month. The calculation
     * rolls around end of year from Esfand to Farvardin.
     *
     *
     * This enum is immutable and unaffected by calling this method.
     *
     * @param months the months to add, positive or negative
     * @return the resulting month, not null
     */
    operator fun plus(months: Long): PersianMonth {
        var amount = (months % 12).toInt()
        // For negative argument
        amount = (amount + 12) % 12
        return values()[(ordinal + amount) % 12]
    }

    /**
     * Returns a month-of-year that is `months` before current month. The calculation
     * rolls around the start of year from Farvardin to Esfand.
     *
     *
     * This enum is immutable and unaffected by calling this method.
     *
     * @param months the months to subtract, positive or negative
     * @return the resulting month, not null
     */
    operator fun minus(months: Long): PersianMonth {
        return plus(-months)
    }

    /**
     * Returns elapsed days from first of the year to first of this month.
     *
     * @return elapsed days from first of the year to first of this month.
     */
    fun daysToFirstOfMonth(): Int {
        val `val` = value
        return if (`val` <= 6) 31 * (`val` - 1) else 30 * (`val` - 1 - 6) + 186
    }

    companion object {
        /**
         * Returns the equivalent instance of `Month`, based on the passed argument.
         * Argument should be from 1 to 12.
         *
         * @param month the number of month
         * @return instance of `Month` enum.
         */
        fun of(month: Int): PersianMonth {
            Utils.intRequireRange(month, 1, 12, "month")
            return values()[month - 1]
        }
    }

}