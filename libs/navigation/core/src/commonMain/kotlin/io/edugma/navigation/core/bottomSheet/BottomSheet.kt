//package io.edugma.navigation.core.bottomSheet
//
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.unit.Dp
//
///**
// * Helper function to create a [ModalBottomSheetLayout] from a [BottomSheetNavigator].
// *
// * @see [ModalBottomSheetLayout]
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//public fun ModalBottomSheet(
//    bottomSheetNavigator: BottomSheetNavigator,
//    modifier: Modifier = Modifier,
//) {
//    ModalBottomSheet(
//        sheetState = bottomSheetNavigator.sheetState,
//        content = bottomSheetNavigator.sheetContent,
//        onDismissRequest = {
//
//        },
//        modifier = modifier,
//    )
//}
