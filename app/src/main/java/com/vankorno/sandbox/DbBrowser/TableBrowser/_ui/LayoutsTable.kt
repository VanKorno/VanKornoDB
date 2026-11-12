package com.vankorno.sandbox.DbBrowser.TableBrowser._ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.vankorno.sandbox.AppStart._data.LayoutModifiers
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.sandbox._ui.composables.Body
import com.vankorno.sandbox._ui.composables.BotBar
import com.vankorno.sandbox._ui.composables.TopBarShadow


@Composable
fun LayoutsTable(                                                      modifiers: LayoutModifiers
) {
    val columns by vm.rawColumns.state()
    val rows by vm.rawRows.state()
    
    val modifBody = modifiers.body
                        .fillMaxWidth()
    
    Body(modifBody) {
        BodyTable(columns, rows)
    }
    TopBarShadow(modifiers.topShadow)
    
    TopTable(modifiers.top)
    
    BotBar(modifiers.bottom) {
        BottomTable()
    }
}

@Composable
fun BottomTable() {
}

@Composable
fun TopTable(modifier: Modifier) {
    
}
