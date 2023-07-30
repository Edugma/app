plugins {
    id("org.jmailen.kotlinter")
}

// TODO Fix formatKotlinAndroidDebug crashes
//tasks.filter {
//    (it.name.startsWith("formatKotlin") ||
//        it.name.startsWith("lintKotlin")) &&
//        it is ConfigurableKtLintTask
//}.forEach {
//    it as ConfigurableKtLintTask
//    it.source = (it.source - fileTree("$buildDir/generated")).asFileTree
//}
//
//fun excludeGeneratedSourceFromLint() {
//    val formatTaskNames = listOf(
//        "formatKotlinCommonMain",
//        "formatKotlinAndroidDebug",
//        "formatKotlinAndroidRelease",
//    )
//    val lintTaskNames = listOf(
//        "lintKotlinCommonMain",
//        "lintKotlinAndroidRelease",
//        "lintKotlinAndroidDebug",
//    )
//    formatTaskNames.forEach {
//        tasks.named<FormatTask>(it) {
//            source = (source - fileTree("$buildDir/generated")).asFileTree
//        }
//    }
//
//    lintTaskNames.forEach {
//        tasks.named<LintTask>(it) {
//            source = (source - fileTree("$buildDir/generated")).asFileTree
//        }
//    }
//}
