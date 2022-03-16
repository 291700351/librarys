package io.github.lee.core.vm.exceptions

import android.content.Context
import io.github.lee.core.vm.R


class ResultThrowable(code: Int, msg: String) : Throwable(msg) {
    var code: Int = code
        private set
    var error: String = msg
        private set

    override fun toString(): String =
        "Error code : %d, Error message : %s".format(code, error)


    fun formatString(context: Context) =
        context.getString(R.string.core_response_throwable_format).format(code, error)
}


//===Desc:=====================================================================================
fun resultThrowable(code: Int, msg: String) =
    ResultThrowable(code, msg)

fun normalThrowable(msg: String) =
    ResultThrowable(1000, msg)

fun resultIsNull() =
    resultThrowable(1001, "Response data is null")

fun resultIsEmpty() =
    resultThrowable(1002, "Response data is null")

fun systemError(msg: String) =
    resultThrowable(1003, msg)

fun unknownError() =
    resultThrowable(1004, "Unknown mistake")