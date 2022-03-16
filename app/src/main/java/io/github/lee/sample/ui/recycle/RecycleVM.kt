package io.github.lee.sample.ui.recycle

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lee.core.vm.BaseRefreshViewModel
import javax.inject.Inject

data class AA(val text: String)

@HiltViewModel
class RecycleVM @Inject constructor(application: Application) :
    BaseRefreshViewModel<String>(application) {

    override fun onRefreshOrLoadMore(isRefresh: Boolean) {
        super.onRefreshOrLoadMore(isRefresh)

        val list = mutableListOf<String>()
        for (i in 0..20) {
            if (isRefresh) {
                list.add("Refresh item : $i")
            } else {
                list.add("Load more item : $i")
            }
        }
        if (isRefresh) {
            sendRefreshData(list)
        } else {
            sendLoadMoreData(list,true)
        }


    }


}