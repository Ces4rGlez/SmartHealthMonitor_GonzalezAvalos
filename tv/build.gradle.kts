plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mx.utng.cfga.smarthealthmonitor.tv"

    // Mantenemos la API 36 con su configuración de compilación actual
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "mx.utng.cfga.smarthealthmonitor.tv"
        minSdk = 21
        targetSdk = 36 // Forzamos a que el objetivo sea la API 36
        versionCode = 1
        versionName = "2.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Desactivamos las buildFeatures de Compose para que herede el entorno clásico de la guía
    buildFeatures {
        compose = false
    }
}

dependencies {
    // 1. Librerías Core esenciales que ya maneja tu catálogo de versiones
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // 2. REQUERIDO: Leanback Library para la UI clásica de Android TV (Soportada en API 36)
    implementation("androidx.leanback:leanback:1.2.0")

    // 3. REQUERIDO: Glide para cargar imágenes en las tarjetas (cards)
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // 4. CRÍTICO: Enlace para heredar Room, MockData y Repositorios desde tu módulo del teléfono
    implementation(project(":app"))

    // 5. REQUERIDO: Soporte para ciclo de vida, ViewModels y Corrutinas en Fragments tradicionales
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.fragment:fragment-ktx:1.8.5") // Habilita la delegación "by viewModels()" en MainFragment
}
