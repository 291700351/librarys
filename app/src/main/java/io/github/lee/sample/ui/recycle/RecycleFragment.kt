package io.github.lee.sample.ui.recycle

import android.os.Bundle
import androidx.navigation.findNavController
import com.drake.brv.utils.BRV
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import dagger.hilt.android.AndroidEntryPoint
import io.github.lee.core.databinding.ui.refresh.RecyclerViewFragment
import io.github.lee.core.vm.status.RefreshData
import io.github.lee.sample.BR
import io.github.lee.sample.R
import io.github.lee.sample.databinding.ToolbarGlobalCenterTitleBinding

@AndroidEntryPoint
class RecycleFragment : RecyclerViewFragment<String, RecycleVM>() {

    override fun onCreateViewModel(): RecycleVM = getCacheViewModel(RecycleVM::class.java)

    override fun onCreateToolbar() = ToolbarGlobalCenterTitleBinding
        .inflate(layoutInflater)
        .apply {
            title = "RecyclerView示例"
        }
        .tl
    //===Desc:=====================================================================================

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        enableLoadMore = true
    }

    override fun onSetViewData() {
        super.onSetViewData()
        setupStatusBar {
            fitsSystemWindows(true)
        }
        setupToolbar {
            setNavigationIcon(R.drawable.ic_round_arrow_back_ios_24)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }

        BRV.modelId = BR.item
        onSetupRecyclerView {
            linear()
            setup {
                addType<String>(R.layout.item_recycle)
            }
            val value = vm?.data?.value
            if (value is RefreshData.RefreshData<*>) {
                models = value.data
            }
        }

    }
}

