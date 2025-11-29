package com.vankorno.sandbox.scr_Table

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.composables.barBot.BackBtn
import com.vankorno.vankornocompose.composables.barBot.LibBotBar
import com.vankorno.vankornocompose.composables.barTop.LibTopBarShadow
import com.vankorno.vankornocompose.composables.body.LibBody
import com.vankorno.vankornocompose.values.LibLayoutModifiers


@Composable
fun LayoutsTable(                                                      modifiers: LibLayoutModifiers
) {
    val columns by vm.rawColumnsFlow.collectAsStateWithLifecycle()
    val rows by vm.rawRowsFlow.collectAsStateWithLifecycle()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
    
    LibBody(modifBody) {
        BodyTable(columns, rows)
    }
    LibTopBarShadow(modifiers.topShadow)
    
    TopTable(modifiers.top)
    
    LibBotBar(modifiers.bottom) {
        BottomTable()
    }
}

@Composable
fun BottomTable() {
    BackBtn()
}

@Composable
fun TopTable(modifier: Modifier) {
    
}