package io.github.lee.core.databinding.ui.refresh

import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.databinding.ViewDataBinding
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import io.github.lee.core.databinding.ui.BaseFragment
import io.github.lee.core.databinding.ui.databinding.LayoutRefreshScrollviewBinding
import io.github.lee.core.vm.BaseScrollViewModel

abstract class NestedScrollViewFragment<VB : ViewDataBinding, VM : BaseScrollViewModel>
    : BaseFragment<LayoutRefreshScrollviewBinding, VM>() {

    protected var enableRefresh = true
    protected var autoRefresh = true

    protected var refreshLayout: SmartRefreshLayout? = null
    protected var scrollViewLayout: NestedScrollView? = null

    protected var showScrollviewVb: VB? = null
    override fun onCreateSuccess() = LayoutRefreshScrollviewBinding.inflate(layoutInflater)

    /**子类实现*/
    protected abstract fun onCreateShowScrollView(): VB?
    //===Desc:=====================================================================================

    override fun onSetViewListener() {
        super.onSetViewListener()
        refreshLayout = vb?.refreshLayout
        if (enableRefresh) {
            refreshLayout?.setOnRefreshListener {
                Log.e("TAG", "ddd")
                vm?.onRefreshOrLoadMore(true)
            }
        }
    }

    override fun onSetViewData() {
        super.onSetViewData()
        refreshLayout?.apply {
            setEnableRefresh(enableRefresh)
            setEnableLoadMore(false)
            if (enableRefresh) {
                this.setRefreshHeader(onCreateRefreshHeader() ?: MaterialHeader(mContext))
            }
        }

        showScrollviewVb = onCreateShowScrollView()
        val root = showScrollviewVb?.root

        scrollViewLayout = vb?.scrollView
        if (root is NestedScrollView) {
            scrollViewLayout?.removeAllViews()
            if (root.childCount == 1) {
                val child = root.getChildAt(0)
                    ?: throw RuntimeException("NestedScrollView get child view is null")
                val p = child.layoutParams
                root.removeView(child)
                child.layoutParams = p
                scrollViewLayout?.addView(child)
            } else {
                throw RuntimeException("NestedScrollView can only have one child View")
            }
        } else {
            scrollViewLayout?.addView(root)
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

}