package com.vankorno.sandbox._ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.vankorno.vankornohelpers.values.LibColors.GlassBlack
import com.vankorno.vankornohelpers.values.LibLambdas.hideKeyboard


@Composable
fun Body(                                                modifier: Modifier,
                                                          content: @Composable ColumnScope.()->Unit,
) = Body(modifier, {}, content)



@Composable
fun Body(                                                modifier: Modifier,
                                                          onClick: ()->Unit,
                                                          content: @Composable ColumnScope.()->Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Column(
        modifier
            .combinedClickable(
                onClick = {
                    hideKeyboard()
                    onClick()
                },
                interactionSource = interactionSource,
                indication = null
            )
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}


@Composable
fun TopBarShadow(                                                            modifier: Modifier
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
