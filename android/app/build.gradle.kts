import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.edugma.android-app")
    kotlin("android")
    id("mp-lint")
    alias(libs.plugins.jetbrains.composePlugin)
    alias(libs.plugins.jetbrains.compose.compiler)
    alias(libs.plugins.tracer) version libs.versions.tracer.get()
}
// TODO signing

val versionsProperties = Properties()
versionsProperties.load(FileInputStream(rootProject.file("configs/versions.properties")))

android {
    namespace = "com.edugma.android"

    defaultConfig {
        applicationId = "com.edugma.android"
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = versionsProperties.getProperty("versionCode").toInt()
        versionName = versionsProperties.getProperty("versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        // We use a bundled debug keystore, to allow debug builds from CI to be upgradable
        named("debug") {
            storeFile = rootProject.file("configs/debug.jks")
            storePassword = "edugma"
            keyAlias = "edugmadebugkey"
            keyPassword = "edugma"
        }
        create("release") {
            val path: String = gradleLocalProperties(rootDir, providers).getProperty("signing.path", "")
            if (path.isNotEmpty()) {
                storeFile = rootProject.file(path)
                storePassword = gradleLocalProperties(rootDir, providers)
                    .getProperty("signing.store.password")
                keyAlias = gradleLocalProperties(rootDir, providers).getProperty("signing.key.alias")
                keyPassword = gradleLocalProperties(rootDir, providers).getProperty("signing.key.password")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"

            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
        maybeCreate("qa").apply {
            matchingFallbacks.add("release")
            applicationIdSuffix = ".qa"

            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            // TODO
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "Edugma-${versionName}-${buildType.name}.apk"
        }
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    buildFeatures {
        buildConfig = true
    }
}

val properties = Properties().apply {
    load(rootProject.file("secrets-dev.properties").reader())
}
val tracerPluginToken = properties.getProperty("edugma.tracer.token.plugin").orEmpty()
val tracerAppToken = properties.getProperty("edugma.tracer.token.app").orEmpty()

tracer {
    create("defaultConfig") {
        pluginToken = ""
        appToken = ""
    }

    create("release") {
        pluginToken = System.getenv("TRACER_PROD_PLUGIN_TOKEN").orEmpty()
        appToken = System.getenv("TRACER_PROD_APP_TOKEN").orEmpty()

        uploadMapping = true
    }

    create("qa") {
        pluginToken = tracerPluginToken
        appToken = tracerAppToken

        uploadMapping = true
    }

    create("debug") {
        pluginToken = tracerPluginToken
        appToken = tracerAppToken

        uploadMapping = false
    }
}

dependencies {
    implementation(project(":shared:app"))
    implementation(project(":android:resources"))
    implementation(projects.shared.core.navigation)
    implementation(projects.shared.core.api)
    implementation(projects.shared.core.analytics)
    //implementation(projects.android.schedule.appwidget)

    implementation(project.dependencies.platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.lifecycle.viewmodel)
    //implementation("androidx.compose.runtime:runtime-tracing:1.0.0-alpha03")

    // tracer
    implementation(libs.tracer.crash)
    implementation(libs.tracer.heap)
    implementation(libs.tracer.disk)
//    implementation(libs.tracer.profiler.sampling)
//    implementation(libs.tracer.profiler.systrace)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espressoCore)
    androidTestImplementation(project.dependencies.platform(libs.compose.bom))
    androidTestImplementation(libs.compose.uiTest)
}

configurations.all {
    resolutionStrategy.eachDependency {
        val requestedGroup = this.requested.group
        if (requestedGroup.startsWith("io.ktor")) {
            useVersion(libs.versions.ktor.get())
        }
    }
}
