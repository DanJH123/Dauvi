import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.dapps.dauvi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dapps.dauvi"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        //Load API keys from apikeys.properties
        val keystoreFile = project.rootProject.file("apikey.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val apiKey = properties.getProperty("WEATHER_API_KEY") ?: ""
        buildConfigField(
            type = "String",
            name = "WEATHER_API_KEY",
            value = apiKey
        )
    }

    buildTypes {
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    ksp {
        arg("room.schemaLocation", "${project.projectDir}/schemas")
        // dagger hilt correctErrorTypes

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    testImplementation(libs.hilt.testing)
    androidTestImplementation(libs.hilt.testing)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.google.fonts)

    implementation(libs.accompanist.permissions)

    implementation(libs.play.services.location)
    implementation(libs.kotlinx.coroutines.play.services)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}