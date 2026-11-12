package com.vankorno.sandbox._ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BotBar(                                                 modifier: Modifier,
                                                             content: @Composable RowScope.()->Unit,
) {
    val gradientBot = listOf(Color.Transparent, Color.Black)
    
    Row(
        modifier
            .heightIn(min = 30.dp)
            .background(brush = Brush.verticalGradient(gradientBot))
            .padding(3.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        content()
    }
}
