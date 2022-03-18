plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

dependencies {
    // ViewModel Support
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    api("androidx.lifecycle:lifecycle-common-java8:2.4.1")
}