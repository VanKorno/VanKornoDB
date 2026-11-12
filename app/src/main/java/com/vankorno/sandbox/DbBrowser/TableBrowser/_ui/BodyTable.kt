package com.vankorno.sandbox.DbBrowser.TableBrowser._ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.sandbox._ui.composables.Spa_______________cer
import com.vankorno.sandbox._ui.theme.AccentColor
import com.vankorno.sandbox._ui.theme.AppColor
import kotlin.math.ceil


@Composable
fun BodyTable(                                                          columns: List<String>,
                                                                           rows: List<List<String>>,
) {
    val columnTypes by vm.rawTypes.state()
    
    if (columns.isEmpty()) {
        Text("This table is as empty as a head of a russian fascist", color = Color.White)
        Spa_______________cer(150)
        return
    }
    
    val colOnPage = 3
    
    val pageCount = ceil(columns.size / colOnPage.toFloat()).toInt()
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scrollState = rememberScrollState()
    
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val colStart = page * colOnPage
        val colEnd = minOf(colStart + colOnPage, columns.size)
        val pageColumns = columns.subList(colStart, colEnd)
        val pageTypes = columnTypes.subList(colStart, colEnd)
        
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth()
                    .background(AccentColor.Blue.color)
            ) {
                pageTypes.forEach { type ->
                    Text(
                        type,
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, AppColor.Background.color)
                        ,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth()
                    .background(AccentColor.Yellow.color)
            ) {
                pageColumns.forEach { col ->
                    Text(
                        col,
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, AppColor.Background.color)
                            .padding(4.dp)
                        ,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
            
            
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                rows.forEach { row ->
                    Row {
                        row.subList(colStart, colEnd).forEach { cell ->
                            Text(
                                cell,
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, Color.Gray)
                                    .padding(4.dp)
                                ,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                            )
                        }
                    }
                }
                Spa_______________cer(150)
            }
        }
    }
}





