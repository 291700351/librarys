package io.github.lee.sample.ui.home

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lee.core.vm.BaseRefreshViewModel
import io.github.lee.sample.model.domain.HomeFunctions
import io.github.lee.sample.model.repository.SoftwareFunctionRepository
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val repo: SoftwareFunctionRepository,
    application: Application
) : BaseRefreshViewModel<HomeFunctions>(application) {

    fun getHomeData() {
        sendRefreshData(repo.getHomeFunctions())
    }

}