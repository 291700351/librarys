package io.github.lee.sample.ui.home

import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import dagger.hilt.android.AndroidEntryPoint
import io.github.lee.core.databinding.ui.refresh.RecyclerViewFragment
import io.github.lee.sample.R
import io.github.lee.sample.databinding.ToolbarGlobalCenterTitleBinding
import io.github.lee.sample.model.domain.HomeFunctions

@AndroidEntryPoint
class HomeFragment : RecyclerViewFragment<HomeFunctions, HomeVM>() {

    override fun onCreateViewModel(): HomeVM = getCacheViewModel(HomeVM::class.java)


    override fun onCreateToolbar(): Toolbar =
        ToolbarGlobalCenterTitleBinding.inflate(layoutInflater).apply {
            title = "示例程序"
        }.tl
    //===Desc:=====================================================================================

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        enableRefresh = false
        enableLoadMore = false

    }

    override fun onSetViewData() {
        super.onSetViewData()
        setupStatusBar {
            fitsSystemWindows(true)
            statusBarColorInt(Color.WHITE)
            statusBarDarkFont(true)
        }
        onSetupRecyclerView {
            linear()
            setup {
                addType<HomeFunctions>(R.layout.item_home_function)
                onClick(R.id.itemRoot) {
                     findNavController().navigate(getModel<HomeFunctions>().targetActionId)
                }
            }
        }

        vm?.getHomeData()
    }

}