package ir.hamraa.androidcommons.core

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.SparseArray
import androidx.annotation.ColorInt

object ColorFilters {

    private val filters = SparseArray<ColorFilter>()

    fun filter(@ColorInt color: Int): ColorFilter {
        var colorFilter = filters[color]
        if (colorFilter != null) {
            return colorFilter
        }
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        filters.put(color, colorFilter)
        return colorFilter
    }
}