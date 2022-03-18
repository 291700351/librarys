package io.github.lee.core.commont.utils.ext

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.Disposable
import coil.request.ImageRequest
import coil.transform.Transformation


/**
 * XML中使用 error_res="@{@drawable/layer_default_img_load}"  不能使用@mipmap
 */
@BindingAdapter(
    value = ["load_url",
        "crossfade_time",
        "placeholder_res",
        "error_res",
        "download_listener"],
    requireAll = false
)
fun ImageView.loadImage(
    url: String? = null,
    crossFadeTime: Int?,
    placeholder: Drawable?,
    error: Drawable?,
    listener: ImageRequest.Listener? = null
): Disposable? =
    loadImageWithTransform(url, crossFadeTime, placeholder, error, listener)


fun ImageView.loadImageWithTransform(
    url: String? = null,
    crossFadeTime: Int?,
    placeholder: Drawable?,
    error: Drawable?,
    listener: ImageRequest.Listener? = null,
    vararg transform: Transformation
): Disposable? {
    if (TextUtils.isEmpty(url)) {
        if (null != error) {
            setImageDrawable(error)
        }
        return null
    }
    return load(url) {
        if (null != crossFadeTime && crossFadeTime > 0) {
            crossfade(true)
            crossfade(crossFadeTime)
        }
        if (null != placeholder) {
            placeholder(placeholder)
        }
        if (transform.isNotEmpty()) {
            transformations(*transform)
        }
        if (null != error) {
            error(error)
        }
        listener(listener)
    }
}
