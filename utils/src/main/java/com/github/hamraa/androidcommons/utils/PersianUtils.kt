package com.github.hamraa.androidcommons.utils

object PersianUtils {

    private val DIGITS = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹", "٬", "٫")

    fun toFarsi(number: String): String {
        return number
            .replace("0", DIGITS[0])
            .replace("1", DIGITS[1])
            .replace("2", DIGITS[2])
            .replace("3", DIGITS[3])
            .replace("4", DIGITS[4])
            .replace("5", DIGITS[5])
            .replace("6", DIGITS[6])
            .replace("7", DIGITS[7])
            .replace("8", DIGITS[8])
            .replace("9", DIGITS[9])
            .replace(",", DIGITS[10])
            .replace(".", DIGITS[11])
    }

    fun toEnglish(number: String): String {
        return number
            .replace(DIGITS[0], "0")
            .replace(DIGITS[1], "1")
            .replace(DIGITS[2], "2")
            .replace(DIGITS[3], "3")
            .replace(DIGITS[4], "4")
            .replace(DIGITS[5], "5")
            .replace(DIGITS[6], "6")
            .replace(DIGITS[7], "7")
            .replace(DIGITS[8], "8")
            .replace(DIGITS[9], "9")
    }
}