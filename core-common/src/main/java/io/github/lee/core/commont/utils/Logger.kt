package io.github.lee.core.commont.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


private class LogAdapter(private val isLoggable: Boolean, formatStrategy: FormatStrategy) :
    AndroidLogAdapter(formatStrategy) {
    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return isLoggable
    }
}

fun initLogger(tag: String, isLoggable: Boolean) {
    val strategy = PrettyFormatStrategy.newBuilder()
        .tag(tag)
        .showThreadInfo(true)
        .build()
    Logger.addLogAdapter(LogAdapter(isLoggable, strategy))
}


fun d(msg: Any?) {
    Logger.d(msg)
}

fun d(msg: String, vararg args: Any) {
    Logger.d(msg, *args)
}

fun e(msg: String, vararg args: Any) {
    Logger.e(msg, *args)
}

fun e(throwable: Throwable?, msg: String, vararg args: Any) {
    Logger.e(throwable, msg, *args)
}

fun w(msg: String, vararg args: Any) {
    Logger.w(msg, *args)
}

fun i(msg: String, vararg args: Any) {
    Logger.i(msg, *args)
}

fun wtf(msg: String, vararg args: Any) {
    Logger.wtf(msg, *args)
}

fun log(msg: Any?, vararg args: Any) {
    e(msg?.toString() ?: "Print message is null", *args)
}


fun json(json: String?) {
    if (json.isNullOrEmpty()) {
        log("The json is null or empty, Please check")
        return
    }
    if (!isJson(json)) {
        log(json)
        return
    }
    Logger.json(json)
}

fun xml(xml: String?) {
    if (xml.isNullOrEmpty()) {
        log("The xml is null or empty, Please check")
        return
    }
    if (!isXml(xml)) {
        log(xml)
        return
    }
    Logger.xml(xml)
}