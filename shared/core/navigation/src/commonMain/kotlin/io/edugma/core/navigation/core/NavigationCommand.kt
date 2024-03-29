package io.edugma.core.navigation.core

import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenBundle

/**
 * Navigation command describes screens transition.
 */
sealed interface NavigationCommand {

    /**
     * Opens new screen.
     */
    data class Forward(val screen: ScreenBundle) : NavigationCommand

    /**
     * Replaces the current screen.
     */
    data class Replace(val screen: ScreenBundle) : NavigationCommand

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
    data class BackTo(val screen: Screen?) : NavigationCommand
}
