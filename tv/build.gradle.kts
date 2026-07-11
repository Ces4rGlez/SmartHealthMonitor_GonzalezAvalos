plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
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
        minSdk = 23
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
        compose = true
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

    // 4. CRÍTICO: Modelos de datos para el catálogo
    // (Copiados localmente para evitar dependencias circulares con el módulo app)
    // implementation(project(":app")) // Eliminado para evitar errores de Manifest Merger y Dynamic Features

    // 5. REQUERIDO: Soporte para ciclo de vida, ViewModels y Corrutinas en Fragments tradicionales
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.fragment:fragment-ktx:1.8.5") // Habilita la delegación "by viewModels()" en MainFragment

    // 6. Jetpack Compose para TV
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation(libs.androidx.activity.compose)
    implementation("androidx.navigation:navigation-compose:2.8.8")

    // 7. Media3 para Reproducción de Video
    val media3Version = "1.5.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")
}
