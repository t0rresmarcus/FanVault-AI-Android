plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    //id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
}

android {
    namespace = "com.fanvault.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fanvault.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Android emulator -> your computer localhost.
        // On a real phone use your computer LAN IP, e.g. "http://192.168.1.20:8000/"
        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8000/\"")
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildToolsVersion = "34.0.0"
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Room (local chat threads)
    implementation("androidx.room:room-runtime:2.8.5")
    implementation("androidx.room:room-ktx:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}
