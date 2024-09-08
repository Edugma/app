import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("io.gitlab.arturbosch.detekt")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

dependencies {
    detektPlugins(libs.detekt.formatting)
}

tasks.register<Detekt>("detektFormat") {
    autoCorrect = true
}

tasks.withType<DetektCreateBaselineTask> {
    setSource(files(projectDir))
    config.setFrom(
        files(
            "$rootDir/configs/detekt/main.yml",
            "$rootDir/configs/detekt/formatting.yml",
        ),
    )
    baseline.set(file("$rootDir/configs/detekt-baseline.xml"))

    include("**/*.kt", "**/*.kts")
    exclude(
        "**/resources/**",
        "**/build/**",
    )

    parallel = true
    buildUponDefaultConfig = true
    allRules = true

    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "1.8"
}

tasks.withType<Detekt> {
    reports {
        html.required.set(false)
        xml.required.set(false)
        txt.required.set(false)
    }

    setSource(files(projectDir))
    config.setFrom(
        files(
            "$rootDir/configs/detekt/main.yml",
            "$rootDir/configs/detekt/formatting.yml",
        ),
    )
    //baseline.set(file("/detekt-baseline.xml"))

    include("**/*.kt", "**/*.kts")
    exclude(
        "**/resources/**",
        "**/build/**",
    )

    parallel = true
    buildUponDefaultConfig = true
    allRules = true

    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "1.8"
}
