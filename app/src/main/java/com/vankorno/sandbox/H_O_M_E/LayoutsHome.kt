package com.vankorno.sandbox.H_O_M_E

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vankorno.sandbox.AppStart._data.LayoutModifiers
import com.vankorno.sandbox._ui.composables.Body
import com.vankorno.sandbox._ui.composables.BotBar
import com.vankorno.sandbox._ui.composables.TopBarShadow

@Composable
fun LayoutsHome(                                                       modifiers: LayoutModifiers
) {
    val scrollState = rememberScrollState()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 50.dp, horizontal = 10.dp)
    
    Body(modifBody) {
        BodyHome()
    }
    
    TopBarShadow(modifiers.topShadow)
    
    TopHome(modifiers.top)
    
    BotBar(modifiers.bottom) {
        BottomHome()
    }
}

@Composable
fun BottomHome() {
    
}

@Composable
fun TopHome(modifier: Modifier) {
    
}

