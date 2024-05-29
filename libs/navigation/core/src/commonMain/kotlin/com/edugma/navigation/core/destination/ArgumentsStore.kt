package com.edugma.navigation.core.destination

interface ArgumentsStore {
    operator fun <T> get(key: String): T?
}
