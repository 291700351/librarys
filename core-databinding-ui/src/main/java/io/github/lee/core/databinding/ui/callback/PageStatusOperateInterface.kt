package io.github.lee.core.databinding.ui.callback

import androidx.annotation.StringRes
import io.github.lee.core.vm.exceptions.ResultThrowable

interface PageStatusOperateInterface {
    fun showLoading()
    fun showSuccess()
    fun showEmpty(throwable: ResultThrowable? = null)
    fun showError(throwable: ResultThrowable? = null)

    fun refresh()
    fun refreshSuccess()
    fun refreshFail(throwable: ResultThrowable? = null)
    fun loadMore()
    fun loadMoreComplete()
    fun loadMoreEnd()
    fun loadMoreFail(data: Throwable? = null)


    fun showProgress()
    fun hideProgress()

    fun toast(msg: String, isLong: Boolean = false)
    fun toastRes(@StringRes stringId: Int, isLong: Boolean = false)

}