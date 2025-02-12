import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.fivebetserio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fivebetserio"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "api_key", gradleLocalProperties(rootDir, providers).getProperty("api_key"))
        resValue("bool", "debug_mode", gradleLocalProperties(rootDir, providers).getProperty("debug_mode"))

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
}

configurations.all {
    exclude(group = "com.google.protobuf", module = "protobuf-java")
    exclude(group = "com.google.protobuf", module = "protobuf-lite")
}

dependencies {


    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-firestore")

    implementation(libs.tools.core)
    implementation(libs.play.services.tasks)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.commons.validator)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.play.services.auth)
    implementation(libs.room.runtime)
    implementation(libs.retrofit)
    implementation (libs.logging.interceptor)
}