package io.github.lee.core.commont.utils.ext

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

inline fun <reified T> Fragment.getArgumentValue(key: String, defaultValue: T?): T? {
    val bundle = arguments ?: return defaultValue
    return when (T::class) {
        Int::class -> bundle.getInt(key) as T
        Long::class -> bundle.getLong(key) as T
        Short::class -> bundle.getShort(key) as T
        Byte::class -> bundle.getByte(key) as T
        Char::class -> bundle.getChar(key) as T
        Double::class -> bundle.getDouble(key) as T
        Float::class -> bundle.getFloat(key) as T
        String::class -> bundle.getString(key) as T
        Boolean::class -> bundle.getBoolean(key) as T
        else -> defaultValue
    }
}

//inline fun <reified T> AppCompatActivity.getBundleList(key: String ): Array<T> {
//    val bundle = intent.extras ?: return emptyArray()
//
//    return when (T::class) {
//        Int::class -> bundle.getIntArray(key)
//        Long::class -> bundle.getLongArray(key)
//        Short::class -> bundle.getShortArray(key) ?: emptyArray ()
//        Byte::class -> bundle.getByteArray(key) ?: emptyArray ()
//        Double::class -> bundle.getDoubleArray(key) ?: emptyArray ()
//        Float::class -> bundle.getFloatArray(key) ?: emptyArray ()
//        String::class -> bundle.getStringArray(key) ?: emptyArray ()
//        Boolean::class -> bundle.getBooleanArray(key) ?: emptyArray ()
//        else -> emptyArray<T>()
//    }
//}

inline fun <reified T> Fragment.getArgumentSerializable(key: String): T? {
    val bundle = arguments ?: return null
    val serializable = bundle.getSerializable(key)
    return if (serializable is T) {
        serializable
    } else {
        null
    }
}

inline fun <reified T : Parcelable> Fragment.getArgumentParcelable(key: String): T? {
    val bundle = arguments ?: return null
    return bundle.getParcelable(key)
}

inline fun <reified TARGET : Activity> Fragment.intentTo(vararg bundles: Pair<String, Any?>) {
    val starter = Intent(activity, TARGET::class.java)
    starter.putExtras(bundleOf(*bundles))
    startActivity(starter)
}