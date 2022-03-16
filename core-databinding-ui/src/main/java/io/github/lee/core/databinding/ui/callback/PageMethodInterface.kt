package io.github.lee.core.databinding.ui.callback

import android.os.Bundle
import io.github.lee.core.vm.BaseViewModel

interface PageMethodInterface<VM : BaseViewModel> {


    fun onCreateViewModel(): VM? = null

    fun onInitData(savedInstanceState: Bundle?) {}
    fun onObserve() {}
    fun onSetViewListener() {}
    fun onSetViewData() {}

}