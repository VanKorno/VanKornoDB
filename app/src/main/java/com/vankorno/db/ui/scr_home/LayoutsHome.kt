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
        BodyMain(modifBody)
    }
    
    TopBarShadow(modifiers.topShadow)
    
    TopMain(modifiers.top)
    
    SharedBarBot(modifiers.bottom) {
        BottomMain()
    }
}

@Composable
fun BottomMain() {
    TODO("Not yet implemented")
}

@Composable
fun TopMain(x0: Modifier) {
    TODO("Not yet implemented")
}
