package com.github.hamraa.androidcommons.time

import java.time.DateTimeException
import java.time.chrono.Era
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange

/**
 * An era in the Persian calendar system.
 *
 *
 * The Persian calendar system has only one era covering the
 * proleptic years greater than zero.
 *
 *
 * **Do not use `ordinal()` to obtain the numeric representation of `PersianEra`.
 * Use `getValue()` instead.**
 */
enum class PersianEra : Era {

    /**
     * The singleton instance for the current era, 'Anno Hegirae Solar',
     * which has the numeric value 1.
     */
    AHS;

    //-----------------------------------------------------------------------

    /**
     * Gets the numeric era `int` value.
     *
     *
     * The era AH has the value 1.
     *
     * @return the era value, 1 (AH)
     */
    override fun getValue(): Int {
        return 1
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the range of valid values for the specified field.
     *
     *
     * The range object expresses the minimum and maximum valid values for a field.
     * This era is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     *
     *
     * If the field is a [ChronoField] then the query is implemented here.
     * The `ERA` field returns the range.
     * All other `ChronoField` instances will throw an `UnsupportedTemporalTypeException`.
     *
     *
     * If the field is not a `ChronoField`, then the result of this method
     * is obtained by invoking `TemporalField.rangeRefinedBy(TemporalAccessor)`
     * passing `this` as the argument.
     * Whether the range can be obtained is determined by the field.
     *
     *
     * The `ERA` field returns a range for the one valid Persian era.
     *
     * @param field  the field to query the range for, not null
     * @return the range of valid values for the field, not null
     * @throws DateTimeException if the range for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     */
    // override as super would return range from 0 to 1
    override fun range(field: TemporalField): ValueRange {
        return if (field === ChronoField.ERA) ValueRange.of(1, 1)
        else super.range(field)
    }

    companion object {
        //-----------------------------------------------------------------------
        /**
         * Obtains an instance of `PersianEra` from an `int` value.
         *
         *
         * The current era, which is the only accepted value, has the value 1
         *
         * @param persianEra  the era to represent, only 1 supported
         * @return the PersianEra. AHS singleton, not null
         * @throws DateTimeException if the value is invalid
         */
        fun of(persianEra: Int): PersianEra {
            return if (persianEra == 1) {
                AHS
            } else {
                throw DateTimeException("Invalid era: $persianEra")
            }
        }
    }
}