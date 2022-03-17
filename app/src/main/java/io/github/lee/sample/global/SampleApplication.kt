package io.github.lee.sample.global

import android.app.Application
import android.util.Log
import com.drake.brv.utils.BRV
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import dagger.hilt.android.HiltAndroidApp
import io.github.lee.sample.BR

@HiltAndroidApp
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        /*设置RecyclerView使用Databinding的默认参数名称*/
        BRV.modelId = BR.item

//
   }
}