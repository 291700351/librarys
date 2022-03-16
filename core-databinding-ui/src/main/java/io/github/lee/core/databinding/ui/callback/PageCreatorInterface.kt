package io.github.lee.core.databinding.ui.callback

import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding

interface PageCreatorInterface<VB : ViewDataBinding> {
    fun onCreateToast(): Toast? = null


    fun onCreateToolbar(): Toolbar? = null
    
    fun onCreateLoading(): ViewDataBinding? = null
    fun onCreateSuccess(): VB? = null
    fun onCreateEmpty(): ViewDataBinding? = null
    fun onCreateError(): ViewDataBinding? = null

}