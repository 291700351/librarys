package io.github.lee.core.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.lee.core.vm.exceptions.*
import io.github.lee.core.vm.status.PageStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application),
    ViewModelLifecycle {
    protected val mContext
        get() = getApplication<Application>()

    protected val currentState = PageStatus.Loading

    /*uiState聚合页面的全部UI 状态*/
    private val _uiState = Channel<PageStatus>() // = Channel()
    val uiState = _uiState.receiveAsFlow()

    //    private val _uiState: MutableStateFlow<PageStatus> =
    //        MutableStateFlow(currentState)
    //    val uiState = _uiState.asStateFlow()

    //===Desc:About UI status=====================================================================================
    /**设置界面状态*/
    protected fun setPageState(builder: () -> PageStatus) {
        launchOnUI {
            _uiState.send(builder())
        }
    }

    //===Desc:协程=====================================================================================

    fun launchOnUI(
        handler: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(handler) { block() }
    }

    suspend fun <T> withContextIO(
        block: suspend CoroutineScope.() -> T
    ) = withContext(Dispatchers.IO) { block }

    suspend fun <T> withContextMain(block: suspend CoroutineScope.() -> T) =
        withContext(Dispatchers.Main) { block }

    fun <T> launchRequest(block: suspend CoroutineScope.() -> T?) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            launchOnUI {
                if (throwable is ResultThrowable) {
                    setPageState { PageStatus.Error(throwable) }

                } else {
                    val msg = throwable.message
                    if (msg.isNullOrEmpty()) {
                        setPageState { PageStatus.Error(unknownError()) }
                    } else {
                        setPageState { PageStatus.Error(systemError(msg)) }
                    }
                }
            }
        }

        launchOnUI(handler) {
            val result = withContext(Dispatchers.IO) { block() }
            if (null == result) {
                setPageState { PageStatus.Error(resultIsNull()) }
            } else {
                if (result is List<*>) {
                    if (result.isNullOrEmpty()) {
                        setPageState { PageStatus.Empty(resultIsEmpty()) }
                    }
                } else {
                    setPageState { PageStatus.Success }
                }
            }
        }
    }
}