package com.vankorno.sandbox.ui.scr_home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vankorno.sandbox.ui.ScrModifiers
import com.vankorno.vankornocompose.composables.barBot.LibBotBar
import com.vankorno.vankornocompose.composables.barTop.LibTopBarShadow
import com.vankorno.vankornocompose.composables.body.LibBody

@Composable
fun LayoutsHome(                                                            modifiers: ScrModifiers
) {
    val scrollState = rememberScrollState()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
    
    LibBody(modifBody) {
        BodyHome(modifBody)
    }
    
    LibTopBarShadow(modifiers.topShadow)
    
    TopHome(modifiers.top)
    
    LibBotBar(modifiers.bottom) {
        BottomHome()
    }
}

@Composable
fun BottomHome() {
    
}

@Composable
fun TopHome(x0: Modifier) {
    
}
