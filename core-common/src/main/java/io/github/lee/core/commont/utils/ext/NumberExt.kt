package io.github.lee.core.commont.utils.ext

import android.content.Context
import android.util.TypedValue
import androidx.annotation.Px

fun Float.toDp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

fun Int.toDp(context: Context): Float {
    return toFloat().toDp(context)
}

fun Double.toDp(context: Context): Float {
    return toFloat().toDp(context)
}

//===Desc:sp=====================================================================================
fun Float.toSp(context: Context): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )


fun Int.toSp(context: Context): Float {
    return toFloat().toSp(context)
}

fun Double.toSp(context: Context): Float {
    return toFloat().toSp(context)
}

//===Desc:=====================================================================================

/**
 * dp转px
 */
@Px
fun Float.toPx(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}
/**
 * dp转px
 */
@Px
fun Int.toPx(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}
/**
 * dp转px
 */
@Px
fun Double.toPx(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}
