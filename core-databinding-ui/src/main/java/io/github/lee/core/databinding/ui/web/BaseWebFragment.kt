package io.github.lee.core.databinding.ui.web

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebViewClient
import io.github.lee.core.databinding.ui.BaseFragment
import io.github.lee.core.databinding.ui.databinding.LayoutBaseWebBinding

/**
 * WebView网页加载基类，可以在Application中调用
 * ``` kotlin
 * BaseWebFragment.initX5Sdk(context)进行初始化
 * ```
 * @see BaseWebFragment.initX5Sdk
 */

abstract class BaseWebFragment<VM : BaseWebViewModel> : BaseFragment<LayoutBaseWebBinding, VM>() {

    companion object {
        fun initX5Sdk(context: Context) {
            QbSdk.initX5Environment(context.applicationContext, object : QbSdk.PreInitCallback {
                override fun onCoreInitFinished() {
                    Log.e("TAG", "X5 core init finished")
                }

                override fun onViewInitFinished(p0: Boolean) {
                    Log.e("TAG", "X5 WebView init finished,Is use X5 : $p0")

                }
            })
            // 在调用TBS初始化、创建WebView之前进行如下配置
            QbSdk.initTbsSettings(
                mapOf(
                    TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
                    TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true
                )
            )
        }
    }

    protected var enableRefresh = true
        set(value) {
            vb?.refresh?.setEnableRefresh(value)
            field = value
        }

    override fun onCreateSuccess() = LayoutBaseWebBinding.inflate(layoutInflater)

    protected abstract fun loadUrl(): String

    override fun onDestroyView() {
        super.onDestroyView()
        vb?.webView?.destroy()
    }
    //===Desc:=====================================================================================

    override fun onObserve() {
        super.onObserve()
        vm?.apply {
            webViewStatus.observe(viewLifecycleOwner) {
                when (it) {
                    is WebViewStatus.StartLoad -> onStartLoad()
                    is WebViewStatus.LoadProgress -> onLoadProgress(it.progress)
                    is WebViewStatus.LoadCompile -> onLoadCompile()
                }
            }
        }
    }

    override fun onSetViewListener() {
        super.onSetViewListener()
        if (enableRefresh) {
            vb?.refresh?.setOnRefreshListener {
                vb?.webView?.reload()
            }
        }
    }

    override fun onSetViewData() {
        super.onSetViewData()
        if (null == vm) {
            getProgressBar()?.visibility = View.GONE
        }
        //设置刷新
        vb?.refresh?.apply {
            setEnableRefresh(enableRefresh)
            if (enableRefresh) {
                this.setRefreshHeader(onCreateRefreshHeader() ?: MaterialHeader(mContext))
            }
        }

        vb?.webView?.settings?.apply {
            setSupportZoom(true)
            builtInZoomControls = false
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
            textZoom = 100
            loadsImagesAutomatically = true
            setSupportMultipleWindows(false)
            blockNetworkImage = false //是否阻塞加载网络图片  协议http or https
            allowFileAccess = false //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
            setAllowFileAccessFromFileURLs(false) //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            setAllowUniversalAccessFromFileURLs(false) //允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            javaScriptCanOpenWindowsAutomatically = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            loadWithOverviewMode = true
            useWideViewPort = true
            domStorageEnabled = true
            setNeedInitialFocus(true)
            defaultTextEncodingName = "utf-8"
            defaultFontSize = 16
            setGeolocationEnabled(true)
            databaseEnabled = true
            setAppCacheEnabled(true)
        }

        vb?.webView?.apply {
            webChromeClient = onCreateWebChromeClient() ?: X5WebChromeClient(vm)
            webViewClient = onCreateWebViewClient() ?: X5WebViewClient(vm)

            vb?.webView?.loadUrl(loadUrl())
        }
    }

    //===Desc:=====================================================================================
    /**子类实现下拉刷新布局，默认使用MaterialHeader*/
    protected open fun onCreateRefreshHeader(): RefreshHeader? = null

    protected open fun onCreateWebChromeClient(): WebChromeClient? = null
    protected open fun onCreateWebViewClient(): WebViewClient? = null

    //===Desc:=====================================================================================

    protected fun setupWebSetting(block: WebSettings.() -> Unit) {
        //设置WebSetting
        vb?.webView?.settings?.apply {
            block()
        }
    }


    protected fun getProgressBar() = vb?.progressBar
    protected fun getRefreshLayout() = vb?.refresh
    protected fun getWebView() = vb?.webView


    /**开始加载网页*/
    protected open fun onStartLoad() {
        if (canGoBack() == true) {
            getProgressBar()?.apply {
                max = 100
                visibility = View.VISIBLE
            }
        }
    }

    /**页面加载进度*/
    protected open fun onLoadProgress(progress: Int) {
        getProgressBar()?.apply {
            this.progress = progress
            if (progress >= 10) {
                onLoadCompile()
            }
        }
    }

    /**页面加载完成*/
    protected open fun onLoadCompile() {
        getProgressBar()?.apply {
            this.progress = 100
            visibility = View.GONE
        }
        getRefreshLayout()?.finishRefresh()
    }
    //===Desc:=====================================================================================

    protected fun canGoBack() = vb?.webView?.canGoBack()
    protected fun canGoBackOrForward(index: Int) = vb?.webView?.canGoBackOrForward(index)
    protected fun canGoForward() = vb?.webView?.canGoForward()

    protected fun goBack() = vb?.webView?.goBack()
    protected fun goBackOrForward(index: Int) = vb?.webView?.goBackOrForward(index)
    protected fun goForward() = vb?.webView?.goForward()


}