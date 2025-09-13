plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.farmapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.farmapp"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
//
//dependencies {
//    // AndroidX Core + AppCompat + Material + ConstraintLayout
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation("androidx.appcompat:appcompat:1.7.0")
//    implementation("com.google.android.material:material:1.12.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//
//    // Lifecycle (MVVM)
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
//
//    // Navigation Components (optional if fragments use karega)
//    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
//    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
//
//    // Retrofit + Gson (API calls ke liye)
//    implementation("com.squareup.retrofit2:retrofit:2.11.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
//
//    // OkHttp (Networking + Logging)
//    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
//    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
//
//    // Coroutines (Background work ke liye)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
//
//    // Play Services Location (GPS current location ke liye)
//    implementation("com.google.android.gms:play-services-location:21.3.0")
//
//    // Gson (JSON parsing ke liye)
//    implementation("com.google.code.gson:gson:2.11.0")
//
//    // Glide (Image loading ke liye)
//    implementation("com.github.bumptech.glide:glide:4.16.0")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
//
//    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
//
//    // Test Dependencies
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}

dependencies {
    // AndroidX Core + AppCompat + Material + ConstraintLayout
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lifecycle (MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

    // Navigation Components
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Play Services Location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.androidx.contentpager)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //ml kit translator
    implementation("com.google.mlkit:translate:17.0.2")

    
    implementation ("androidx.cardview:cardview:1.0.0")
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
