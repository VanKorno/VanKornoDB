package com.vankorno.sandbox.scr_Table

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr


@Composable
fun BodyTable(                                                          columns: List<String>,
                                                                           rows: List<List<String>>,
) {
    val columnTypes by vm.rawTypesFlow.collectAsStateWithLifecycle()
    
    if (columns.isEmpty() || rows.isEmpty()) {
        Text("The table is empty", color = Color.White)
        Spa_______________cerEndScr()
        return
    }

    // Header row (first 3 columns only)
    Row {
        columns.take(3).forEach { col ->
            Text(
                col,
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.White)
                    .padding(4.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Data rows (first 3 columns only)
    rows.forEach { row ->
        Row {
            row.take(3).forEach { cell ->
                Text(
                    cell,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.White)
                        .padding(4.dp),
                    color = Color.White
                )
            }
        }
    }

    Spa_______________cerEndScr()
}





