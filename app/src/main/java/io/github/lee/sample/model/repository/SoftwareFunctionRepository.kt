package io.github.lee.sample.model.repository

import io.github.lee.sample.R
import io.github.lee.sample.model.domain.HomeFunctions
import javax.inject.Inject


class SoftwareFunctionRepository @Inject constructor() {

    fun getHomeFunctions(): List<HomeFunctions> {
        return mutableListOf<HomeFunctions>()
            .apply {
                add(HomeFunctions("RecyclerView列表", R.id.action_homeFragment_to_recycleFragment))
                add(HomeFunctions("ScrollView", R.id.action_homeFragment_to_scrollFragment))
                add(HomeFunctions("WebView", R.id.action_homeFragment_to_webFragment))
            }
    }

}