package io.edugma.navigation.core.router

import io.edugma.navigation.core.screen.Destination
import io.edugma.navigation.core.screen.DestinationBundle

/**
 * Navigation command describes screens transition.
 */
sealed interface NavigationCommand {

    /**
     * Opens new screen.
     */
    data class Forward(val screen: DestinationBundle<*>) : NavigationCommand

    /**
     * Replaces the current screen.
     */
    data class Replace(val screen: DestinationBundle<*>) : NavigationCommand

    /**
     * Rolls fragmentBack the last transition from the screens chain.
     */
    object Back : NavigationCommand

    /**
     * Rolls fragmentBack to the needed screen from the screens chain.
     *
     * Behavior in the case when no needed screens found depends on an implementation of the
     * But the recommended behavior is to return to the root.
     */
    data class BackTo(val destination: Destination?) : NavigationCommand
}
