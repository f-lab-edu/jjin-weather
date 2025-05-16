import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.jin.jjinweather"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jin.jjinweather"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localPropertiesFile = rootProject.file("local.properties")
        var openWeatherApiKey = ""
        var chatGPTApiKey = ""
        when {
            localPropertiesFile.exists() -> {
                val properties = Properties().apply {
                    load(localPropertiesFile.inputStream())
                }
                openWeatherApiKey = properties.getProperty("OPEN_WEATHER_API_KEY") ?: ""
                chatGPTApiKey = properties.getProperty("CHAT_GPT_API_KEY") ?: ""
            }

            System.getenv("OPEN_WEATHER_API_KEY") != null -> {
                openWeatherApiKey = System.getenv("OPEN_WEATHER_API_KEY") ?: ""
            }

            System.getenv("CHAT_GPT_API_KEY") != null -> {
                chatGPTApiKey = System.getenv("CHAT_GPT_API_KEY") ?: ""
            }

            else -> {
                openWeatherApiKey = ""
                chatGPTApiKey = ""
            }
        }

        defaultConfig {
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$openWeatherApiKey\"")
            buildConfigField("String", "CHAT_GPT_API_KEY", "\"$chatGPTApiKey\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.splash.screen)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)

    implementation(libs.accompanist.permissions)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.androidx.datastore)
    implementation(libs.compose.foundation.pager)
    implementation(libs.androidx.compose.material)
}
