package ir.hamraa.androidcommons.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.abs

object NumberUtils {

    private val WORDS_0_19 = arrayOf(
        "صفر",
        "یک",
        "دو",
        "سه",
        "چهار",
        "پنج",
        "شش",
        "هفت",
        "هشت",
        "نه",
        "ده",
        "یازده",
        "دوازده",
        "سیزده",
        "چهارده",
        "پانزده",
        "شانزده",
        "هفده",
        "هجده",
        "نوزده"
    )

    private val WORDS_10_90 =
        arrayOf("ده", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")

    private val WORDS_100_900 = arrayOf(
        "صد",
        "دویست",
        "سیصد",
        "چهارصد",
        "پانصد",
        "ششصد",
        "هفتصد",
        "هشتصد",
        "نهصد"
    )

    private val WORDS_1000 = arrayOf("هزار", "میلیون", "میلیارد")

    fun getCharOccurance(input: String, ch: Char): Int {
        var i = 0
        for (c in input.toCharArray()) {
            if (c == ch) {
                i++
            }
        }
        return i
    }

    fun extractDigits(input: String): String {
        return input.replace("\\D+".toRegex(), "")
    }

    fun percentFormat(value: Double): String {
        return DecimalFormat("##.##").format(value * 100.0) + "٪"
    }

    fun percentDownloadFormat(value: Double): String {
        return DecimalFormat("##").format(value * 100.0) + "٪"
    }

    fun valueFormat(value: Float): String {
        return DecimalFormat("###.##").format(value.toDouble())
    }

    fun format(value: Double): String {
        return DecimalFormat("###,###,###,###").format(BigDecimal.valueOf(value))
    }

    fun indexFormat(value: Double?): String {
        return DecimalFormat("###,###,###").format(value)
    }

    fun format(value: Long?): String {
        return DecimalFormat("###,###,###,###").format(value)
    }

    fun format(value: String): String {
        return DecimalFormat("###,###,###,###").format(value.toLong())
    }

    fun toShortValue(value: Long): String {
        var value = value
        var space = " "

        if (value < 0) {
            space = "- "
        }

        value = abs(value)

        if (value < 1000) {
            return valueFormat(value.toFloat() / 1000.0f) + space
        }

        return when {
            value < 1000000 -> valueFormat(value.toFloat() / 1000.0f) + space + "هزار"
            value < 1000000000 -> valueFormat(value.toFloat() / 1000000.0f) + space + "میلیون"
            else -> valueFormat(value.toFloat() / 1.0E9f) + space + "میلیارد"
        }
    }

    fun toWords(value: Long): String {
        var value = value
        if (value <= 999) {
            return below999(value)
        }
        var word: String? = null
        var i = 0
        while (value > 0) {
            var temp: String?
            if (value % 1000 != 0L) {
                temp = below999(value % 1000)
                if (i > 0) {
                    temp = temp + " " + WORDS_1000[i - 1]
                }
                if (word != null) {
                    temp = "$temp و $word"
                }
            } else {
                temp = word
            }
            value /= 1000
            i++
            word = temp
        }
        return "$word تومان "
    }

    private fun below999(value: Long): String {
        if (value <= 99) {
            return below99(value % 100)
        }
        return if (value % 100 == 0L) WORDS_100_900[value.toInt() / 100 - 1]
        else WORDS_100_900[value.toInt() / 100 - 1] + " و " + below99(value % 100)
    }

    private fun below99(value: Long): String {
        if (value < 20) {
            return WORDS_0_19[value.toInt()]
        }
        val word = WORDS_10_90[value.toInt() / 10 - 1]
        return if (value % 10 != 0L) word + " و " + WORDS_0_19[value.toInt() % 10] else word
    }
}