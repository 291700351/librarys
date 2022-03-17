package io.github.lee.core.databinding.ui.web

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.lee.core.vm.BaseViewModel

interface UiWebViewStatus
sealed class WebViewStatus {
    object StartLoad : UiWebViewStatus
    data class LoadProgress(val progress: Int) : UiWebViewStatus
    object LoadCompile : UiWebViewStatus


}

open class BaseWebViewModel(application: Application) : BaseViewModel(application) {
    private val _webViewStatus = MutableLiveData<UiWebViewStatus>()
    val webViewStatus: LiveData<UiWebViewStatus> = _webViewStatus

    fun startLoad() {
        _webViewStatus.postValue(WebViewStatus.StartLoad)
    }

    fun loadProgress(progress: Int) {
        _webViewStatus.postValue(WebViewStatus.LoadProgress(progress))
    }

    fun loadCompile() {
        _webViewStatus.postValue(WebViewStatus.LoadCompile)
    }

}