package com.vankorno.sandbox.scr_Playground

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.vankorno.vankornocompose.composables.Spa_______________cerEndScr
import com.vankorno.vankornocompose.sp1
import com.vankorno.vankornocompose.theme_main.LibColor


@Composable
fun BodyPlayground(                                                             modifier: Modifier
) {
    TempText("Sorry, there's nothing here yet")
    
    
    
    
    Spa_______________cerEndScr()
}




@Composable
private fun TempText(                                                         text: String
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

