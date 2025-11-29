package com.vankorno.sandbox.scr_DbBrowser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.navig.ScrTable
import com.vankorno.sandbox.navig.goTo
import com.vankorno.vankornocompose.composables.Spa_______________cer
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr
import com.vankorno.vankornocompose.sp1
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.MOD_W90


@Composable
fun BodyDbBrowser() {
    val internalTables by vm.internalTablesFlow.collectAsStateWithLifecycle()
    val appTables by vm.appTablesFlow.collectAsStateWithLifecycle()
    
    
    TableListTitle("Internal Tables")
    Spa_______________cer(20)
    
    internalTables.forEach { table ->
        TableBtn(table, LibAccentColor.LightGreen.color)
    }
    Spa_______________cer(70)
    
    
    
    TableListTitle("App Tables")
    Spa_______________cer(20)
    
    appTables.forEach { table ->
        TableBtn(table, LibAccentColor.LightBlue.color)
    }
    
    
    
    Spa_______________cerEndScr()
}



@Composable
private fun TableBtn(                                                              table: String,
                                                                                   color: Color,
) {
    Row(
        MOD_W90
            .padding(vertical = 3.dp)
            .background(color, RoundedCornerShape(10.dp))
            .clickable(
                onClick = {
                    vm.currTable = table
                    dbh.goTo(ScrTable)
                }
            )
            .padding(vertical = 15.dp, horizontal = 10.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = table,
            color = LibColor.WhiteText.color,
            fontSize = 16.sp1(),
            maxLines = 3,
        )
    }
}


@Composable
private fun TableListTitle(                                                         text: String
) {
    Text(
        text = text,
        color = LibColor.WhiteText.color,
        fontSize = 24.sp1(),
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        textAlign = TextAlign.Center,
    )
    
}


