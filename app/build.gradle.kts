plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}


android {
    compileSdk = project.ext.get("compileSdk").toString().toInt()

    defaultConfig {
        applicationId = "io.github.lee.sample"
        minSdk = project.ext.get("minSdk").toString().toInt()
        targetSdk = project.ext.get("targetSdk").toString().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }
    buildTypes {
        debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    //    val coreVersion = "1.0.1"
    //    implementation("io.github.291700351:core-view-model:${coreVersion}")
    //    implementation("io.github.291700351:core-databinding-ui:${coreVersion}") {
    //        exclude("io.github.291700351")
    //    }
    implementation(project(":core-view-model"))
    implementation(project(":core-databinding-ui")) {
        exclude("io.github.291700351")
    }

    // Navigation Support
    val navVersion = "2.4.0"
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

    // Hilt Support
    implementation("com.google.dagger:hilt-android:2.41")
    kapt("com.google.dagger:hilt-android-compiler:2.41")

    // Runtime Permissions Support
    val permissionsDispatcher = "4.9.1"
    implementation("com.github.permissions-dispatcher:permissionsdispatcher:${permissionsDispatcher}")
    kapt("com.github.permissions-dispatcher:permissionsdispatcher-processor:${permissionsDispatcher}")
}