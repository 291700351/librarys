package io.github.lee.core.databinding.ui.refresh

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.addModels
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import io.github.lee.core.databinding.ui.BaseFragment
import io.github.lee.core.databinding.ui.databinding.LayoutRefreshRecyclerBinding
import io.github.lee.core.databinding.view.ExtendLoadMoreView
import io.github.lee.core.vm.BaseRefreshViewModel
import io.github.lee.core.vm.exceptions.ResultThrowable
import io.github.lee.core.vm.exceptions.resultIsEmpty
import io.github.lee.core.vm.status.RefreshData


open class RecyclerViewFragment<T : Any, VM : BaseRefreshViewModel<T>>
    : BaseFragment<LayoutRefreshRecyclerBinding, VM>() {

    protected var enableRefresh = true
    protected var enableLoadMore = true
    protected var autoRefresh = true


    protected var refreshLayout: SmartRefreshLayout? = null

    protected var recycleView: RecyclerView? = null


    //===Desc:=====================================================================================

    override fun onCreateSuccess() = LayoutRefreshRecyclerBinding.inflate(layoutInflater)

    //===Desc:=====================================================================================

    override fun onObserve() {
        super.onObserve()
        vm?.data?.observe(viewLifecycleOwner) {
            when (it) {
                is RefreshData.RefreshData<*> -> {
                    val list = it.data
                    if (list.isNullOrEmpty()) {
                        cleanData()
                        showEmpty(resultIsEmpty())
                    } else {
                        setNewData(it.data)
                        // getRecyclerView()?.models = it.data
                    }
                    refreshSuccess()
                }
                is RefreshData.RefreshError -> {
                    //  getRecyclerView()?.models = emptyList()
                    cleanData()
                    showEmpty(it.throwable)
                }
                is RefreshData.LoadMoreData<*> -> {
                    addData(it.data)
                    //   getRecyclerView()?.addModels(it.data)
                    if (it.hasMore) {
                        loadMoreComplete()
                    } else {
                        loadMoreEnd()
                    }
                }
                is RefreshData.LoadMoreError -> {
                    loadMoreFail(it.throwable)
                }
            }
        }
    }

    override fun onSetViewListener() {
        super.onSetViewListener()
        refreshLayout = vb?.pageRefreshLayout
        recycleView = vb?.recycleView
        if (enableRefresh) {
            refreshLayout?.setOnRefreshListener {
                vm?.onRefreshOrLoadMore(true)
            }
        }
        if (enableLoadMore) {
            refreshLayout?.setOnLoadMoreListener {
                vm?.onRefreshOrLoadMore(false)
            }
        }
    }

    override fun onSetViewData() {
        super.onSetViewData()
        refreshLayout?.apply {
            setEnableRefresh(enableRefresh)
            setEnableLoadMore(enableLoadMore)

            if (enableRefresh) {
                this.setRefreshHeader(onCreateRefreshHeader() ?: MaterialHeader(mContext))
            }
            if (enableLoadMore) {
                this.setRefreshFooter(onCreateLoadMoreFooter() ?: ExtendLoadMoreView(mContext))
                refreshLayout?.setEnableLoadMoreWhenContentNotFull(true)
            }
        }

        if (enableRefresh) {
            if (autoRefresh) {
                refreshLayout?.autoRefresh()
            }
        }

    }

    //===Desc:=====================================================================================
    /**子类实现下拉刷新布局，默认使用MaterialHeader*/
    protected open fun onCreateRefreshHeader(): RefreshHeader? = null

    /**子类实现加载更多布局 默认使用ClassicsFooter*/
    protected open fun onCreateLoadMoreFooter(): RefreshFooter? = null


    //===Desc:=====================================================================================

    override fun refresh() {
        //super.refresh()
    }

    override fun refreshSuccess() {
        refreshLayout?.finishRefresh(true)
    }

    override fun refreshFail(throwable: ResultThrowable?) {
        refreshLayout?.finishRefresh(false)
    }

    override fun loadMore() {

    }

    override fun loadMoreComplete() {
        refreshLayout?.finishLoadMore(true)
    }

    override fun loadMoreEnd() {
        refreshLayout?.finishLoadMoreWithNoMoreData()
    }

    override fun loadMoreFail(data: Throwable?) {
        refreshLayout?.finishLoadMore(false)
    }

    //===Desc:=====================================================================================


    //===Desc:=====================================================================================


    protected fun cleanData() {
        recycleView?.models = emptyList()
    }

    protected fun setNewData(data: List<*>) {
        recycleView?.models = data
    }

    protected fun addData(data: List<*>?, animation: Boolean = true) {
        recycleView?.addModels(data, animation)
    }

    protected fun addData(item: T?, animation: Boolean = true) {
        recycleView?.addModels(listOf(item), animation)
    }


    //
    //    //===Desc:=====================================================================================
    //
    protected fun onSetupRecyclerView(block: RecyclerView.() -> Unit) {
        recycleView?.apply {
            block()
            if (null == layoutManager) {
                linear()
            }
        }
    }

    //
    //    //===Desc:=====================================================================================


}