package com.vankorno.sandbox.Playground

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vankorno.vankornocompose.composables.barBot.BackBtn
import com.vankorno.vankornocompose.composables.barBot.LibBotBar
import com.vankorno.vankornocompose.composables.barTop.LibTopBarShadow
import com.vankorno.vankornocompose.composables.body.LibBody
import com.vankorno.vankornocompose.values.LibLayoutModifiers


@Composable
fun LayoutsPlayground(                                                 modifiers: LibLayoutModifiers
) {
    val scrollState = rememberScrollState()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 50.dp, horizontal = 10.dp)
    
    LibBody(modifBody) {
        BodyPlayground()
    }
    
    LibTopBarShadow(modifiers.topShadow)
    
    TopPlayground(modifiers.top)
    
    LibBotBar(modifiers.bottom) {
        BottomPlayground()
    }
}

@Composable
fun BottomPlayground() {
    BackBtn()
}

@Composable
fun TopPlayground(modifier: Modifier) {
    
}