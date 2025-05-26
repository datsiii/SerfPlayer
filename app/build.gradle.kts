plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.dokka") version "1.8.20"
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android") version "2.51.1"
}


tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka"))
}

hilt {
    enableAggregatingTask = false
}

android {
    namespace = "com.example.serfplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.serfplayer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // совместимо с Kotlin 1.9.23
    }
    buildFeatures {
        compose = true
    }
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
        }
        resolutionStrategy.eachDependency {
            if (requested.group == "com.squareup" && requested.name == "javapoet") {
                useVersion("1.13.0")
            }
        }

    }

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("com.squareup:javapoet:1.13.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //exoplayer
    implementation(libs.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.common)

    implementation (libs.ui)
    implementation (libs.androidx.room.runtime)
    implementation (libs.hilt.android)
    implementation (libs.accompanist.systemuicontroller)
    kapt (libs.hilt.compiler)

    // Icons - Extended
    implementation (libs.androidx.material.icons.extended)
    // Navigation
    implementation (libs.androidx.navigation.compose)
    // Room database
    implementation (libs.androidx.room.ktx)
    kapt (libs.androidx.room.compiler)
    // Dagger Hilt - hiltViewModel()
    implementation (libs.androidx.hilt.navigation.compose)
    // Permission
    implementation (libs.accompanist.permissions)
    // Other (System UI, ModalBottomSheet)
    implementation (libs.accompanist.systemuicontroller)
    implementation (libs.accompanist.navigation.material)
    // Coil
    implementation (libs.coil.kt.coil.compose)
    // material3
    implementation (libs.material3)
    implementation (libs.androidx.material3.window.size.class1)
    // Drag and move
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.0")
    // Motion Layout
    implementation (libs.androidx.constraintlayout.compose)
    // System UI
    implementation (libs.accompanist.systemuicontroller.v0170)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}