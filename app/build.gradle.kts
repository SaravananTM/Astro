import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    namespace = "com.horoscope.matcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.horoscope.matcher"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "GEMINI_API_KEY",
            "\"${localProps.getProperty("GEMINI_API_KEY", "")}\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            storePassword = localProps.getProperty("KEYSTORE_PASSWORD", "")
            keyAlias = localProps.getProperty("KEY_ALIAS", "")
            keyPassword = localProps.getProperty("KEY_PASSWORD", "")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
