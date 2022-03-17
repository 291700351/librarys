plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

dependencies {
    // StatusBar Support
    val statusBarVersion = "3.2.1"
    // 基础依赖包，必须要依赖
    api("com.geyifeng.immersionbar:immersionbar:$statusBarVersion")
    // kotlin扩展（可选）
    api("com.geyifeng.immersionbar:immersionbar-ktx:$statusBarVersion")

    api("io.github.291700351:core-view-model:1.0.1")

    // Refresh Support
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Adapter Support
    api("com.github.liangjingkanji:BRV:1.3.54")

    // X5 WebView Support
    api("com.tencent.tbs:tbssdk:44165")
}