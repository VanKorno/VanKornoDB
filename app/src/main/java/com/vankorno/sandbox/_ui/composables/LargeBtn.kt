package com.vankorno.sandbox._ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vankorno.sandbox._ui.theme.AccentColor


@Composable
fun LargeBtn(                                                                 text: String,
                                                                       accentColor: AccentColor,
                                                                           onClick: ()->Unit,
) {
    Column(
        Modifier.fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 30.dp)
            .background(accentColor.color, RoundedCornerShape(15.dp))
            .clickable(
                onClick = onClick
            )
            .padding(vertical = 22.dp, horizontal = 14.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LargeBtnText(text)
    }
}


@Composable
private fun LargeBtnText(                                                           text: String
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(7f, 7f),
                blurRadius = 8f
            )
        )
    )
}
