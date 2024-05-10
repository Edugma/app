package io.edugma.navigation.core.screen

interface ArgumentsStore {
    operator fun <T> get(key: String): T?
}
