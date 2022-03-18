package io.github.lee.core.commont.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt


@ColorInt
fun prettyColor(): Int {
    val r = (50..200).random()
    val g = (50..200).random()
    val b = (50..200).random()
    return Color.rgb(r, g, b)
}

fun gradientDrawable(
    @ColorInt color: Int,
    topLeftRadius: Float = 0F,
    topRightRadius: Float = 0F,
    bottomLeftRadius: Float = 0F,
    bottomRightRadius: Float = 0F
): GradientDrawable {
    val drawable = GradientDrawable()
    drawable.cornerRadii = arrayOf(
        topLeftRadius, topLeftRadius,
        topRightRadius, topRightRadius,
        bottomRightRadius, bottomRightRadius,
        bottomLeftRadius, bottomLeftRadius
    ).toFloatArray()
    drawable.setColor(color)
    return drawable
}

fun gradientDrawable(@ColorInt color: Int, radius: Float = 0F) =
    gradientDrawable(color, radius, radius, radius, radius)

fun isXml(text: String): Boolean = text.startsWith("<") && text.endsWith(">")
fun isJson(text: String): Boolean = text.startsWith("{") && text.endsWith("}")
        || text.startsWith("[") && text.endsWith("]")
