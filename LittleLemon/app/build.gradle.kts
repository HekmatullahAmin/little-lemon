plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

android {
    namespace = "com.hekmatullahamin.littlelemon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hekmatullahamin.littlelemon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.8")
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
//    test dependency for navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
//    test for coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
//    This implementation requires that your project minSDK be set to API level 21 or higher.
//    ModalBottomSheet
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
//    Nav Host
    implementation("androidx.navigation:navigation-compose:2.7.7")

//    Room
    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

//    ktor-client-core
    implementation("io.ktor:ktor-client-core:2.1.3")
//    ktor-client-android
    implementation("io.ktor:ktor-client-android:2.1.3")
//    ktor-client-content-negotiation
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")

    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")

//    Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

//    Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

//    Lottie for animation
    implementation("com.airbnb.android:lottie-compose:6.4.1")
}