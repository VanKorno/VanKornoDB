package com.vankorno.sandbox.theme_main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.sandbox._navig.ScrHome
import com.vankorno.sandbox._navig.Screen
import com.vankorno.vankornocompose.values.MOD_MaxSize
import com.vankorno.vankornohelpers.values.LibLambdas.clearFocus
import com.vankorno.vankornohelpers.values.LibLambdas.hideKeyboard
import com.vankorno.vankornohelpers.values.LibLambdas.showKeyboard

val LocalScreen = compositionLocalOf<Screen> { ScrHome }

@Composable
fun LibMainScaffold(                              statusBarColor: Color = LibColor.BlackSurf.color,
                                                      typography: Typography = Typography(),
                                                         content: @Composable ()->Unit,
) {
    LibMainTheme(
        statusBarColor = statusBarColor,
        typography = typography
    ) {
        Surface(
            modifier = MOD_MaxSize
                .windowInsetsPadding(
                    WindowInsets.systemBars.add(WindowInsets.ime)
                ),
            color = LibColor.Background.color
        ) {
            KeyboardActions()
            
            val currScreen by vm.currScreen.state()
            
            CompositionLocalProvider(
                LocalScreen provides currScreen
            ) {
                content()
            }
        }
    }
}


@Composable
private fun KeyboardActions() {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    
    SideEffect {
        showKeyboard = { keyboard?.show() }
        hideKeyboard = { keyboard?.hide() }
        
        clearFocus = {
            try {
                focusManager.clearFocus()
            } catch (_: Exception) {}
        }
    }
}
