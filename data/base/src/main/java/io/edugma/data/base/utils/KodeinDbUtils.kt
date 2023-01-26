package io.edugma.data.base.utils

import org.kodein.db.DB
import org.kodein.db.OpenPolicy
import org.kodein.db.impl.open
import org.kodein.db.orm.kotlinx.KotlinxSerializer
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.pathString

fun buildDB(pathProvider: PathProvider, serializer: KotlinxSerializer): DB {
    val root = pathProvider.getAbsolutePath()
    val path = Path(root, "kodein", "db")
    if (path.notExists()) {
        path.createDirectories()
    }
    return DB.open(
        path.pathString,
        serializer,
        OpenPolicy.OpenOrCreate,
    )
}
