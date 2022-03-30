package io.edugma.android.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.systemBarsPadding
import io.edugma.features.base.core.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        Box(
                            Modifier
                                .systemBarsPadding()
                                .imePadding()
                        ) {
                            MainContent()
                        }
                    }
                }
            }
        }
    }
}