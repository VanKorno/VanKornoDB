package com.vankorno.db.ui.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.vankorno.vankornocompose.dp1
import com.vankorno.vankornohelpers.values.LibColors.GlassBlack
import com.vankorno.vankornohelpers.values.hideKeyboard


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SharedBody(                                          modifier: Modifier,
                                                      composables: @Composable ColumnScope.()->Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Column(
        modifier
            .combinedClickable(
                onClick = {
                    hideKeyboard()
                },
                interactionSource = interactionSource,
                indication = null
            )
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        composables()
    }
}



@Composable
fun SharedBarBot(                                           modifier: Modifier,
                                                         composables: @Composable RowScope.()->Unit,
) {
    val gradientBot = listOf(Color.Transparent, Color.Black)
    
    Row(
        modifier
            .heightIn(min = 30.dp)
            .background(brush = Brush.verticalGradient(gradientBot))
            .padding(2.dp1())
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        composables()
    }
}


@Composable
fun TopBarShadow(                                                               modifier: Modifier
) {
    Box(
        modifier
            .height(12.dp)
            .background(
                brush = Brush.verticalGradient(listOf(Color(GlassBlack), Color.Transparent)),
                shape = RectangleShape
            )
    ) {}
}



