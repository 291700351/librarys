package io.github.lee.core.vm.status

import io.github.lee.core.vm.exceptions.ResultThrowable


interface UiRefreshDataStatus
sealed class RefreshData {
    data class RefreshData<T>(val data: List<T>) : UiRefreshDataStatus
    data class LoadMoreData<T>(val data: List<T>, val hasMore: Boolean = true) :
        UiRefreshDataStatus

    data class RefreshError(val throwable: ResultThrowable? = null) : UiRefreshDataStatus
    data class LoadMoreError(val throwable: ResultThrowable? = null) : UiRefreshDataStatus
}