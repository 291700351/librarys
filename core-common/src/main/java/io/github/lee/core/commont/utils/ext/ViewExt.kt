package io.github.lee.core.commont.utils.ext

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.databinding.BindingAdapter
import io.github.lee.core.commont.utils.gradientDrawable

@BindingAdapter(value = ["gong"], requireAll = false)
fun View.gong(gong: Boolean?) {
    visibility = if (gong == true) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter(value = ["invisible"], requireAll = false)
fun View.invisible(invisible: Boolean?) {
    visibility = if (invisible == true) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter(
    value = [
        "backdrop_color",
        "corner_top_left",
        "corner_top_right",
        "corner_bottom_right",
        "corner_bottom_left",
    ], requireAll = false
)
fun View.setBackdrop(
    @ColorInt color: Int? = Color.TRANSPARENT,
    @Px cornerTopLeft: Float? = 0F,
    @Px cornerTopRight: Float? = 0F,
    @Px cornerBottomRight: Float? = 0F,
    @Px cornerBottomLeft: Float? = 0F
) {
    gradientDrawable(
        color ?: Color.TRANSPARENT,
        cornerTopLeft ?: 0F, cornerTopRight ?: 0F,
        cornerBottomLeft ?: 0F, cornerBottomRight ?: 0F
    )
}


val View?.viewParent: ViewGroup?
    get() = if (null == this) {
        null
    } else {
        val parent = this.parent
        if (parent is ViewGroup) {
            parent
        } else {
            null
        }
    }

val View?.children: List<View>
    get() = if (null == this) {
        emptyList()
    } else {
        if (this is ViewGroup) {
            (0 until childCount).map {
                getChildAt(it)
            }
        } else {
            emptyList()
        }
    }


fun View?.removeSelfFromParent() {
    if (null == this) {
        return
    }
    val parent = this.parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

fun View?.onViewClick(block: (View.() -> Unit)? = null) {
    if (null == this) {
        return
    }
    if (null == block) {
        this.setOnClickListener(null)
    } else {
        this.setOnClickListener {
            block.invoke(it)
        }
    }
}

