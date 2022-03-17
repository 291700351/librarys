package io.github.lee.sample.ui.web

import io.github.lee.core.databinding.ui.web.BaseWebFragment

class WebFragment : BaseWebFragment<WebVM>() {

    override fun loadUrl(): String = "https://www.baidu.com/"

    override fun onCreateViewModel() = getCacheViewModel(WebVM::class.java)


    override fun onSetViewData() {
        super.onSetViewData()
        setupStatusBar {
            fitsSystemWindows(true)
        }
    }
}