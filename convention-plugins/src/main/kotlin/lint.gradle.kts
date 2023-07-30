import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    id("org.jmailen.kotlinter")
}

tasks.named<FormatTask>("formatKotlinMain") {
    exclude { it.file.path.contains("generated/")}
}

tasks.named<LintTask>("lintKotlinMain") {
    exclude { it.file.path.contains("generated/")}
}
