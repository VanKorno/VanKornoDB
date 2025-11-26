package com.vankorno.db.ui.scr_home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vankorno.db.ui.ScrModifiers
import com.vankorno.db.ui.shared.SharedBarBot
import com.vankorno.db.ui.shared.SharedBody
import com.vankorno.db.ui.shared.TopBarShadow

@Composable
fun LayoutsHome(                                                            modifiers: ScrModifiers
) {
    val scrollState = rememberScrollState()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
    
    SharedBody(modifBody) {
        BodyHome(modifBody)
    }
    
    TopBarShadow(modifiers.topShadow)
    
    TopHome(modifiers.top)
    
    SharedBarBot(modifiers.bottom) {
        BottomHome()
    }
}

@Composable
fun BottomHome() {
    
}

@Composable
fun TopHome(x0: Modifier) {
    
}
