package io.github.lee.core.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.lee.core.vm.exceptions.ResultThrowable
import io.github.lee.core.vm.status.PageStatus
import io.github.lee.core.vm.status.RefreshData
import io.github.lee.core.vm.status.UiRefreshDataStatus

open class BaseScrollViewModel(application: Application) : BaseViewModel(application) {
    open fun onRefreshOrLoadMore(isRefresh: Boolean) {
        if (isRefresh) {
            setPageState { PageStatus.Refresh }
        } else {
            setPageState { PageStatus.LoadMore }
        }
    }
}


open class BaseRefreshViewModel<T : Any>(application: Application) : BaseViewModel(application) {

    open fun onRefreshOrLoadMore(isRefresh: Boolean) {
        if (isRefresh) {
            setPageState { PageStatus.Refresh }
        } else {
            setPageState { PageStatus.LoadMore }
        }
    }

    //===Desc:=====================================================================================

    private val _data = MutableLiveData<UiRefreshDataStatus>()
    val data: LiveData<UiRefreshDataStatus> = _data


    fun sendRefreshData(data: List<T>) =
        _data.postValue(RefreshData.RefreshData(data))


    fun sendRefreshError(throwable: ResultThrowable? = null) =
        _data.postValue(RefreshData.RefreshError(throwable))

    fun sendLoadMoreData(data: List<T>, hasMore: Boolean = true) =
        _data.postValue(RefreshData.LoadMoreData(data, hasMore))


    fun sendLoadMoreError(throwable: ResultThrowable? = null) =
        _data.postValue(RefreshData.LoadMoreError(throwable))


}