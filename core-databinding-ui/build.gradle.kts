plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

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

//    // Android Runtime Permissions
//    api("com.github.tbruyelle:rxpermissions:0.12")
}