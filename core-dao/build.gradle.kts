plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

dependencies {
    val roomVersion = "2.4.1"
    api("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    api("androidx.room:room-ktx:$roomVersion") // 协程操作Room
}