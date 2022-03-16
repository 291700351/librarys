package io.github.lee.core.vm.status

import androidx.annotation.StringRes
import io.github.lee.core.vm.exceptions.ResultThrowable

interface UiStatus

sealed class PageStatus : UiStatus {
    /*About UI show*/
    object Loading : PageStatus()
    object Success : PageStatus()
    data class Empty(val throwable: ResultThrowable? = null) : PageStatus()
    data class Error(val throwable: ResultThrowable? = null) : PageStatus()


    /*About UI refresh or load more*/
    object Refresh : PageStatus()
    object RefreshSuccess  : PageStatus()
    data class RefreshFail(val throwable: ResultThrowable? = null) : PageStatus()

    object LoadMore : PageStatus()
    object LoadMoreComplete  : PageStatus()
    object LoadMoreEnd : PageStatus()
    data class LoadMoreFail(val throwable: ResultThrowable? = null) : PageStatus()

    /*About UI effect*/
    object Progress : PageStatus()
    object HideProgress : PageStatus()
    data class Toast(val msg: String, val isLong: Boolean = false) : PageStatus()
    data class ToastRes(@StringRes val stringId: Int, val isLong: Boolean = false) : PageStatus()

}
