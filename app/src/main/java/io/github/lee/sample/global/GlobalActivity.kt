package io.github.lee.sample.global

import android.view.WindowManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.lee.core.databinding.ui.BaseActivity
import io.github.lee.core.vm.BaseViewModel
import io.github.lee.sample.BuildConfig
import io.github.lee.sample.databinding.ActivityGlobalBinding

@AndroidEntryPoint
class GlobalActivity : BaseActivity<ActivityGlobalBinding, BaseViewModel>() {

    override fun onCreateSuccess(): ActivityGlobalBinding =
        ActivityGlobalBinding.inflate(layoutInflater)

    override fun onSetViewData() {
        super.onSetViewData()
        //debug模式屏幕常亮
        if (BuildConfig.DEBUG) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        setupStatusBar {
            transparentBar()
        }

    }
}