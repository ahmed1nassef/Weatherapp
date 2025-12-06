import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nassef.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room - only what we need
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Coroutines support
    ksp(libs.androidx.room.compiler)

    // Test helpers
    testImplementation(libs.androidx.room.testing)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    implementation(project(":domain"))
    implementation(project(":core"))

    // Gson for JSON serialization
    implementation(libs.gson)
    //Paging 3
    implementation(libs.androidx.paging.runtime)

    // alternatively - without Android dependencies for tests
    testImplementation(libs.androidx.paging.common)

    // optional - Jetpack Compose integration
    implementation(libs.androidx.paging.compose)
}