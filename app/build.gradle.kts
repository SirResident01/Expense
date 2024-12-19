plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android") version "1.8.22" // Явно указываем версию Kotlin
}

android {
    namespace = "com.example.expense"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expense"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas"
                )
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion("1.8.22")
            }
        }
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1")
    implementation("androidx.room:room-runtime:2.5.1")
    annotationProcessor("androidx.room:room-compiler:2.5.1")
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation ("com.github.PhilJay:MPAndroidChart:3.1.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")

    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
