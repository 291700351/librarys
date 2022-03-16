package io.github.lee.sample.global

import android.app.Application
import com.drake.brv.utils.BRV
import dagger.hilt.android.HiltAndroidApp
import io.github.lee.sample.BR

@HiltAndroidApp
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        /*设置RecyclerView使用Databinding的默认参数名称*/
        BRV.modelId = BR.item

//        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> MaterialHeader(context) }
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }
    }
}