package io.github.lee.core.databinding.ui.web

import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

class X5WebChromeClient(private val vm: BaseWebViewModel?) : WebChromeClient() {

    override fun onProgressChanged(p0: WebView?, p1: Int) {
        super.onProgressChanged(p0, p1)
        vm?.loadProgress(p1)
    }
}