package io.github.lee.core.databinding.ui.web

import android.graphics.Bitmap
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class X5WebViewClient(private val vm: BaseWebViewModel?) : WebViewClient() {
    override fun onPageStarted(p0: WebView?, p1: String?, p2: Bitmap?) {
        super.onPageStarted(p0, p1, p2)
        vm?.startLoad()
    }


    override fun onPageFinished(p0: WebView?, p1: String?) {
        super.onPageFinished(p0, p1)
        vm?.loadCompile()
    }
}