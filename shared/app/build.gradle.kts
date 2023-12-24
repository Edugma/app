import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("mp-compose-lib")
    alias(libs.plugins.buildKonfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core.designSystem)
                implementation(projects.shared.core.navigation)
                implementation(projects.shared.core.arch)
                implementation(projects.shared.core.utils)
                implementation(projects.shared.core.system)
                implementation(projects.shared.core.network)

                implementation(projects.shared.nodes)

                implementation(projects.shared.home)

                implementation(projects.shared.schedule.elements)
                implementation(projects.shared.schedule.menu)
                implementation(projects.shared.schedule.daily)
                implementation(projects.shared.schedule.scheduleInfo)
                implementation(projects.shared.schedule.calendar)
                implementation(projects.shared.schedule.lessonsReview)
                implementation(projects.shared.schedule.sources)
                implementation(projects.shared.schedule.history)
                implementation(projects.shared.schedule.freePlace)

                implementation(projects.shared.account)

                implementation(projects.shared.misc.menu)
                implementation(projects.shared.misc.settings)
                implementation(projects.shared.misc.other)


                implementation(projects.shared.schedule.data)
            }
        }
    }
}

val versionsProperties = Properties()
versionsProperties.load(FileInputStream(rootProject.file("configs/versions.properties")))

fun TargetConfigDsl.boolean(name: String, value: Boolean) {
    buildConfigField(type = BOOLEAN, name = name, value = value.toString(), const = true)
}

fun TargetConfigDsl.string(name: String, value: String) {
    buildConfigField(type = STRING, name = name, value = value, const = true)
}

fun TargetConfigDsl.buildConfigFields(
    buildType: String,
    isLogsEnabled: Boolean,
    isNetworkLogsEnabled: Boolean,
    isDebugPanelEnabled: Boolean,
) {
    string("BuildType", buildType)

    boolean("IsLogsEnabled", isLogsEnabled)
    boolean("IsNetworkLogsEnabled", isNetworkLogsEnabled)
    boolean("IsDebugPanelEnabled", isDebugPanelEnabled)
}

buildkonfig {
    packageName = "io.edugma.shared.app"
    exposeObjectWithName = "BuildKonfig"

    defaultConfigs("debug") {
        buildConfigFields(
            buildType = "debug",
            isLogsEnabled = true,
            isNetworkLogsEnabled = true,
            isDebugPanelEnabled = true,
        )
    }
    defaultConfigs("qa") {
        buildConfigFields(
            buildType = "qa",
            isLogsEnabled = true,
            isNetworkLogsEnabled = false,
            isDebugPanelEnabled = true,
        )
    }
    defaultConfigs("release") {
        buildConfigFields(
            buildType = "release",
            isLogsEnabled = false,
            isNetworkLogsEnabled = false,
            isDebugPanelEnabled = false,
        )
    }
    defaultConfigs {
        string("Platform", "")
        string("Version", versionsProperties.getProperty("versionName"))
    }
    targetConfigs {
        create("android") {
            string("Platform", "android")
        }
        create("ios") {
            string("Platform", "ios")
        }
    }
}

android {
    namespace = "io.edugma.features.app"
}
