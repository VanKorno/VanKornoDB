package com.vankorno.sandbox.scr__Home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vankorno.vankornocompose.composables.barBot.LibBotBar
import com.vankorno.vankornocompose.composables.barTop.LibTopBarShadow
import com.vankorno.vankornocompose.composables.body.LibBody
import com.vankorno.vankornocompose.values.LibLayoutModifiers

@Composable
fun LayoutsHome(                                                       modifiers: LibLayoutModifiers
) {
    val scrollState = rememberScrollState()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 50.dp, horizontal = 10.dp)
    
    LibBody(modifBody) {
        BodyHome()
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
fun TopHome(modifier: Modifier) {
    
}
