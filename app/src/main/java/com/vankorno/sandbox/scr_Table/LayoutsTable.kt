package com.vankorno.sandbox.scr_Table

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val scrollState = rememberScrollState()
    
    val columns by vm.rawColumnsFlow.collectAsStateWithLifecycle()
    val rows by vm.rawRowsFlow.collectAsStateWithLifecycle()
    
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 50.dp, horizontal = 10.dp)
    
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