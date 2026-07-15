import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
}

val localProps = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localProps.load(localPropsFile.inputStream())
}

android {
    namespace = "mx.utng.cfga.smarthealthmonitor.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String","NEON_API_KEY",
            "\"${localProps["NEON_API_KEY"]}\"")
        buildConfigField("String","NEON_HOST",
            "\"${localProps["NEON_HOST"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    
    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // MQTT
    api("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    api("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    // Serialization
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Retrofit + OkHttp para llamadas a Neon HTTP API
    api("com.squareup.retrofit2:retrofit:2.11.0")
    api("com.squareup.retrofit2:converter-gson:2.11.0")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // WorkManager
    api("androidx.work:work-runtime-ktx:2.9.1")
}
