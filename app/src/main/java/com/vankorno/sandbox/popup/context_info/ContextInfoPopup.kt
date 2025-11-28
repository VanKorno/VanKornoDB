package com.vankorno.sandbox.popup.context_info

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vankorno.vankornocompose.sp1
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.MOD_W90

@Composable
fun ContextInfoPopup() {
    Text(
        modifier = MOD_W90
             .padding(vertical = 10.dp)
        ,
        text = "Test text",
        fontSize = 18.sp1(),
        textAlign = TextAlign.Start,
        color = LibColor.WhiteText.color
    )
}