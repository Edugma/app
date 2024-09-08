import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    id("io.gitlab.arturbosch.detekt")
}

tasks.register<Detekt>("detektFormat") {
    autoCorrect = true
}

tasks.withType<DetektCreateBaselineTask> {
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/configs/detekt.yml"))
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
    config.setFrom(files("$rootDir/configs/detekt.yml"))
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
