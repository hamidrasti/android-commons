@file:Suppress("unused")

package com.github.hamraa.androidcommons.time

/**
 * This class provides static helper methods, in order to remove boilerplate code.
 * It is not possible to get an instance of this class.
 *
 *
 */
object Utils {

    /**
     * Returns true if and only if value is greater than or equal to `lowerLimit`
     * and is less than or equal to `upperLimit`.
     *
     * @param value        the value to be checked, an integer
     * @param lowerLimit lower boundary to be checked, an integer
     * @param upperLimit upper boundary to be checked, an integer
     * @return true if and only if value is between `lowerLimit` and
     * `upperLimit`
     */
    fun isBetween(value: Int, lowerLimit: Int, upperLimit: Int): Boolean {
        return value in lowerLimit..upperLimit
    }

    /**
     * Returns true if and only if value is greater than or equal to `lowerLimit`
     * and is less than or equal to `upperLimit`.
     *
     * @param value        the value to be checked, a long
     * @param lowerLimit lower boundary to be checked, a long
     * @param upperLimit upper boundary to be checked, a long
     * @return true if and only if value is between `lowerLimit` and
     * `upperLimit`
     */
    fun isBetween(value: Long, lowerLimit: Long, upperLimit: Long): Boolean {
        return value in lowerLimit..upperLimit
    }

    /**
     * Checks whether an integer is in a range or not. If value is less than
     * `lowerLimit` or greater than `upperLimit`, an IllegalArgumentException
     * will be thrown with a suitable message.
     *
     * @param value        value to check
     * @param lowerLimit lower limit of range
     * @param upperLimit upper limit of range
     * @param valName    the name of value that is printed in the exception message
     * @return value, if it is in the range
     */
    fun intRequireRange(value: Int, lowerLimit: Int, upperLimit: Int, valName: String): Int {
        require(isBetween(value, lowerLimit, upperLimit)) {
            "$valName $value is out of valid range [$lowerLimit, $upperLimit]"
        }
        return value
    }

    /**
     * Checks whether an integer is greater than zero or not. If value is less than
     * or equal to zero, an IllegalArgumentException will be thrown with a suitable message.
     *
     * @param value     integer value to check
     * @param valName name of value that is printed in the exception message
     * @return value, if it is positive
     */
    fun intRequirePositive(value: Int, valName: String): Int {
        require(value > 0) { "$valName is not positive: $value" }
        return value
    }

    /**
     * Checks whether a long is greater than zero or not. If value is less than
     * or equal to zero, an IllegalArgumentException will be thrown with a suitable message.
     *
     * @param value     long value to check
     * @param valName name of value that is printed in the exception message
     * @return value, if it is positive
     */
    fun longRequirePositive(value: Long, valName: String): Long {
        require(value > 0) { "$valName is not positive: $value" }
        return value
    }

    /**
     * Checks whether a long is in a range or not. If value is less than
     * `lowerLimit` or greater than `upperLimit`, an IllegalArgumentException
     * will be thrown with a suitable message.
     *
     * @param value        value to check
     * @param lowerLimit lower limit of range
     * @param upperLimit upper limit of range
     * @param valName    the name of value that is printed in the exception message
     * @return value, if it is in the range
     */
    fun longRequireRange(
        value: Long,
        lowerLimit: Long,
        upperLimit: Long,
        valName: String
    ): Long {
        require(!(value < lowerLimit || value > upperLimit)) {
            "$valName $value is out of valid range [$lowerLimit, $upperLimit]"
        }
        return value
    }
}