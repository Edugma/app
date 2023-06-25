@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("org.eclipse.jgit:org.eclipse.jgit:3.5.0.201409260305-r")

import org.eclipse.jgit.api.Git
import java.io.File

println("Start")

fun File.makeDirs() = apply { mkdirs() }

val githubId = "microsoft/fluentui-system-icons"
val repository = "https://github.com/$githubId"
val version = "1.1.204"

val repoCloneDir = createTempDir(prefix = "git-repo")

println("Cloning repository")
val git = Git.cloneRepository()
    .setURI(repository)
    .setDirectory(repoCloneDir)
    .call()
git.checkout().setName("refs/tags/$version").call()

val iconsDir = File(repoCloneDir, "android/library/src/main/res/drawable")

val srcDir = File("src/commonMain/kotlin").apply { mkdirs() }
srcDir.deleteRecursively()
srcDir.mkdirs()

val resourceFolder = File("src/commonMain/resources")
resourceFolder.deleteRecursively()
resourceFolder.mkdirs()

val resourceFileFolder = File(srcDir, "io/edugma/core/icons/")
resourceFileFolder.deleteRecursively()
resourceFileFolder.mkdirs()

val resourcesFile = File(resourceFileFolder, "EdIcons.kt")
resourcesFile.createNewFile()

println("Copying")
val writer = resourcesFile.bufferedWriter()
writer.appendLine("package io.edugma.core.icons")
writer.appendLine()
writer.appendLine("public object EdIcons {")
iconsDir.walkTopDown().filter { it.extension == "xml" }
    .forEach {
        val newFile = File(resourceFolder, it.name)
        val newText = it.readText().replace("@color/fluent_default_icon_tint", "#212121")
        newFile.writeText(newText)
        writer.appendLine("\tpublic const val ${it.name.removeSuffix(".xml")}: String = \"${it.name}\"")
    }
writer.appendLine("}")
writer.close()
println("Finish")
