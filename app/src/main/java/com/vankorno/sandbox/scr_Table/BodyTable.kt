package com.vankorno.sandbox.scr_Table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.MOD_MaxSize
import com.vankorno.vankornocompose.values.MOD_MaxW
import kotlin.math.ceil


@Composable
fun BodyTable(                                                          columns: List<String>,
                                                                           rows: List<List<String>>,
) {
    val columnTypes by vm.rawTypesFlow.collectAsStateWithLifecycle()
    
    if (columns.isEmpty()) {
        Text("This table is as empty as a head of a russian fascist", color = Color.White)
        Spa_______________cerEndScr()
        return
    }
    
    val pageCount = ceil(columns.size / 3f).toInt()
    val pagerState = rememberPagerState(pageCount = { pageCount })
    
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val colStart = page * 3
        val colEnd = minOf(colStart + 3, columns.size)
        val pageColumns = columns.subList(colStart, colEnd)
        val pageTypes = columnTypes.subList(colStart, colEnd)
        
        Column(MOD_MaxSize) {
            Row(
                MOD_MaxW
                    .background(LibAccentColor.Blue.color)
            ) {
                pageTypes.forEach { type ->
                    Text(
                        type,
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, LibColor.Background.color)
                        ,
                        color = Color.White,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Row(
                MOD_MaxW
                    .background(LibAccentColor.Yellow.color)
            ) {
                pageColumns.forEach { col ->
                    Text(
                        col,
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, LibColor.Background.color)
                            .padding(4.dp)
                        ,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            
            
            val scrollState = rememberScrollState()
            
            Column(
                modifier = MOD_MaxSize
                    .verticalScroll(scrollState)
            ) {
                rows.forEach { row ->
                    Row {
                        row.subList(colStart, colEnd).forEach { cell ->
                            Text(
                                cell,
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, Color.LightGray)
                                    .padding(4.dp),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                Spa_______________cerEndScr()
            }
        }
    }
}





