package io.github.lee.sample.ui.web

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lee.core.databinding.ui.web.BaseWebViewModel
import javax.inject.Inject

@HiltViewModel
class WebVM @Inject constructor(application: Application) : BaseWebViewModel(application) {


}