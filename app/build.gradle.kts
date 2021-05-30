import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.mospolytech.mospolyhelper"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 4
        versionName = "0.3.0"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }

//        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    val navVersion = "2.3.5"
    val koinVersion = "3.0.2"
    val ktorVersion = "1.5.4"
    val roomVersion = "2.3.0"
    val logbackVersion = "1.2.3"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))


    // Android
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.fragment:fragment-ktx:1.3.4")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")


    // UI
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.budiyev.android:circular-progress-bar:1.2.2")
    implementation ("com.microsoft.design:fluent-system-icons:1.1.125") {
        exclude(module = "unspecified")
    }


    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0-native-mt")


    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")


    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.3.0")
    testImplementation("org.robolectric:robolectric:4.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")


    // ViewBinding
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.4.6")


    // DI
    // Koin for Kotlin Multiplatform
    implementation("io.insert-koin:koin-core:$koinVersion")
    // Koin Test for Kotlin Multiplatform
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    // Koin main features for Android (Scope,ViewModel ...)
    implementation("io.insert-koin:koin-android:$koinVersion")
    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    // SLF4J Logger
    //implementation("io.insert-koin:koin-logger-slf4j:$koin_version")


    // Network
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.auth0.android:jwtdecode:2.0.0")


    // Json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")


    // Database
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")


    // Html parser
    implementation("org.jsoup:jsoup:1.13.1")


    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.0.0")


    // Image loader
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.bumptech.glide:recyclerview-integration:4.12.0") {
        // Excludes the support library because it"s already included by Glide.
        isTransitive = false
    }
}
