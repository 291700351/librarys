package io.github.lee.core.databinding.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import io.github.lee.core.databinding.ui.R
import io.github.lee.core.databinding.ui.databinding.ViewLoadMoreBinding

@SuppressLint("RestrictedApi")
class ExtendLoadMoreView constructor(private val context: Context) :
    RefreshFooter {

    private var vb: ViewLoadMoreBinding =
        ViewLoadMoreBinding.inflate(LayoutInflater.from(context))


    private var mNoMore = false


    //===Desc:=====================================================================================
    override fun getView(): View = vb.root

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate

    //动画控制
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }


    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        if (success) {
            if (mNoMore) {
                vb.loadStatus = context.getString(R.string.core_load_end)
            } else {
                vb.loadStatus = context.getString(R.string.core_load_complete)
            }
        } else {
            vb.loadStatus = context.getString(R.string.core_load_failed)
        }
        return 500
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        if (!mNoMore) {
            when (newState) {
                RefreshState.None -> {
                    vb.progressBar.visibility = View.GONE
                    vb.loadStatus = context.getString(R.string.core_pull_loading)
                }
                RefreshState.PullUpToLoad -> {
                    vb.loadStatus = context.getString(R.string.core_pull_loading)
                }
                RefreshState.Loading,
                RefreshState.LoadReleased -> {
                    vb.progressBar.visibility = View.VISIBLE
                    vb.progressBar.visibility = View.VISIBLE
                    vb.loadStatus = context.getString(R.string.core_loading)
                }
                RefreshState.ReleaseToLoad -> {
                    vb.progressBar.visibility = View.VISIBLE
                    vb.loadStatus = context.getString(R.string.core_released_for_loading)
                }
                RefreshState.Refreshing -> {
                    vb.progressBar.visibility = View.VISIBLE
                    vb.loadStatus = context.getString(R.string.core_loading)
                }
                RefreshState.LoadFinish -> {
                    vb.progressBar.visibility = View.GONE
                    if (mNoMore) {
                        vb.loadStatus = context.getString(R.string.core_load_end)
                    } else {
                        vb.loadStatus = context.getString(R.string.core_load_complete)
                    }
                }
                else -> {}
            }
        }

    }


    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {

    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }


    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean = false

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMore != noMoreData) {
            mNoMore = noMoreData
            vb.progressBar.visibility = View.GONE
            if (noMoreData) {
                vb.loadStatus = context.getString(R.string.core_load_end)

            } else {
                vb.loadStatus = context.getString(R.string.core_load_complete)
            }
        }
        return true
    }

}