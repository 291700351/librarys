package io.github.lee.core.commont.utils.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

inline fun TextView.onBeforeTextChanged(
    crossinline beforeTextChangedBlock: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChangedBlock(p0, p1, p2, p3)
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

        }
    })
}

inline fun TextView.onTextChanged(
    crossinline textChangedBlock: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            textChangedBlock(p0, p1, p2, p3)
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    })
}

inline fun TextView.onAfterTextChanged(
    crossinline afterTextChangedBlock: (text: Editable?) -> Unit = {},
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            afterTextChangedBlock(p0)
        }
    })
}