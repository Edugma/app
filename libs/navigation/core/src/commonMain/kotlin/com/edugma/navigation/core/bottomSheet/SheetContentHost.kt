//package com.edugma.navigation.core.bottomSheet
//
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.SheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.rememberUpdatedState
//import androidx.compose.runtime.saveable.SaveableStateHolder
//import androidx.compose.runtime.snapshotFlow
//import androidx.navigation.NavBackStackEntry
//import androidx.navigation.compose.LocalOwnersProvider
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.flow.drop
//
///**
// * Hosts a [BottomSheetNavigator.Destination]'s [NavBackStackEntry] and its
// * [BottomSheetNavigator.Destination.content] and provides a [onSheetDismissed] callback. It also
// * shows and hides the [ModalBottomSheetLayout] through the [sheetState] when the sheet content
// * enters or leaves the composition.
// *
// * @param backStackEntry The [NavBackStackEntry] holding the [BottomSheetNavigator.Destination],
// * or null if there is no [NavBackStackEntry]
// * @param sheetState The [ModalBottomSheetState] used to observe and control the sheet visibility
// * @param onSheetDismissed Callback when the sheet has been dismissed. Typically, you'll want to
// * pop the back stack here.
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//internal fun ColumnScope.SheetContentHost(
//    backStackEntry: NavBackStackEntry?,
//    sheetState: SheetState,
//    saveableStateHolder: SaveableStateHolder,
//    onSheetShown: (entry: NavBackStackEntry) -> Unit,
//    onSheetDismissed: (entry: NavBackStackEntry) -> Unit,
//) {
//    if (backStackEntry != null) {
//        val currentOnSheetShown by rememberUpdatedState(onSheetShown)
//        val currentOnSheetDismissed by rememberUpdatedState(onSheetDismissed)
//        LaunchedEffect(sheetState, backStackEntry) {
//            snapshotFlow { sheetState.isVisible }
//                // We are only interested in changes in the sheet's visibility
//                .distinctUntilChanged()
//                // distinctUntilChanged emits the initial value which we don't need
//                .drop(1)
//                .collect { visible ->
//                    if (visible) {
//                        currentOnSheetShown(backStackEntry)
//                    } else {
//                        currentOnSheetDismissed(backStackEntry)
//                    }
//                }
//        }
//        backStackEntry.LocalOwnersProvider(saveableStateHolder) {
//            @Suppress("DEPRECATION")
//            val content = (backStackEntry.destination as BottomSheetNavigator.Destination).content
//            content(backStackEntry)
//        }
//    }
//}
