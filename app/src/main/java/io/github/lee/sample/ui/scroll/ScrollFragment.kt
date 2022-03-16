package io.github.lee.sample.ui.scroll

import android.os.Bundle
import androidx.navigation.findNavController
import io.github.lee.core.databinding.ui.refresh.NestedScrollViewFragment
import io.github.lee.core.vm.BaseScrollViewModel
import io.github.lee.sample.R
import io.github.lee.sample.databinding.FragmentScrollBinding
import io.github.lee.sample.databinding.ToolbarGlobalCenterTitleBinding

class ScrollFragment : NestedScrollViewFragment<FragmentScrollBinding, BaseScrollViewModel>() {
    override fun onCreateShowScrollView() = FragmentScrollBinding.inflate(layoutInflater)

    override fun onCreateToolbar() = ToolbarGlobalCenterTitleBinding
        .inflate(layoutInflater)
        .apply {
            title = "ScrollView示例"
        }
        .tl
    //===Desc:=====================================================================================

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        enableRefresh = false
    }

    override fun onSetViewData() {
        super.onSetViewData()
        setupToolbar {
            setNavigationIcon(R.drawable.ic_round_arrow_back_ios_24)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

}