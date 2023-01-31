package io.edugma.core.designSystem.organism.topAppBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme

/** Contains default values used for the top app bar implementations. */
object EdTopAppBarDefaults {

//    /**
//     * Default insets to be used and consumed by the top app bars
//     */
//    val windowInsets: WindowInsets
//        @Composable
//        get() = WindowInsets.systemBarsForVisualComponents
//            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    /**
     * Creates a [EdTopAppBarColors] for center aligned top app bars. The default implementation
     * animates between the provided colors according to the Material Design specification.
     *
     * @param containerColor the container color
     * @param navigationIconContentColor the content color used for the navigation icon
     * @param titleContentColor the content color used for the title
     * @param actionIconContentColor the content color used for actions
     * @return the resulting [EdTopAppBarColors] used for the top app bar
     */
    @Composable
    fun colors(
        containerColor: Color = EdTheme.colorScheme.surface,
        navigationIconContentColor: Color = EdTheme.colorScheme.onSurface,
        titleContentColor: Color = EdTheme.colorScheme.onSurface,
        actionIconContentColor: Color = EdTheme.colorScheme.onSurfaceVariant,
    ): EdTopAppBarColors =
        EdTopAppBarColors(
            containerColor,
            navigationIconContentColor,
            titleContentColor,
            actionIconContentColor,
        )

    val ContainerHeight = 52.0.dp
}
