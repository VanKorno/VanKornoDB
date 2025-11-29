package com.vankorno.sandbox.scr_Table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr


@Composable
fun BodyTable(                                                         modifier: Modifier,
                                                                        columns: List<String>,
                                                                           rows: List<List<String>>,
) {
    val columnTypes by vm.rawTypesFlow.collectAsStateWithLifecycle()
    
    
    Text("Sorry, there's nothing here yet")
    
    
    
    
    Spa_______________cerEndScr()
}





