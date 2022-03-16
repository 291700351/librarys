package io.github.lee.sample.model.domain

import androidx.annotation.IdRes

data class HomeFunctions(
    val name: String,
    @IdRes val targetActionId: Int
)