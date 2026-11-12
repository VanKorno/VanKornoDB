package com.vankorno.sandbox._ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

@Composable
fun MainTheme(                                              statusBarColor: Color,
                                                                   typography: Typography,
                                                                      content: @Composable ()->Unit,
) {
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = statusBarColor.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = false
        }
    }
    
    MaterialTheme(
        typography = typography,
        content = content
    )
}
