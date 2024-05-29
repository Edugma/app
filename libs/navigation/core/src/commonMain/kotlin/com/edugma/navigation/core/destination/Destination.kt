package com.edugma.navigation.core.destination

abstract class Destination(
    val destinationName: String,
) {
    internal val arguments = mutableSetOf<NavArgument<*>>()
    internal val deepLinks = mutableSetOf<NavDeepLink>()

    fun getArgs(): Set<NavArgument<*>> {
        return arguments
    }

    fun getDeepLinks(): Set<NavDeepLink> {
        return deepLinks
    }
}

abstract class NoArgDestination(name: String) : Destination(name) {
    operator fun invoke(): DestinationBundle<*> = toBundle()
}
