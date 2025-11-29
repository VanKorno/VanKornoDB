package com.vankorno.sandbox.scr__Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.unit.dp
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.navig.ScrDbBrowser
import com.vankorno.sandbox.navig.ScrPlayground
import com.vankorno.sandbox.navig.goTo
import com.vankorno.vankornocompose.actions.applyIf
import com.vankorno.vankornocompose.dp3
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.sp2
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.LocalScrType


@Composable
fun BodyHome(                                                                   modifier: Modifier
) {
    NavBtns()
    
    
}


@Composable
private fun NavBtns() {
    NavBtn(ScrPlayground, "Playground", LibAccentColor.Pink)
    
    NavBtn(ScrDbBrowser, "DB Browser", LibAccentColor.Green)
}


@Composable
private fun NavBtn(                                                      targetScr: Screen,
                                                                              text: String,
                                                                       accentColor: LibAccentColor,
) {
    val scrType = LocalScrType.current
    
    Column(
        Modifier
            .applyIf(
                condition = scrType.isPortrait,
                trueBlock = {
                    fillMaxWidth()
                },
                falseBlock = {
                    sizeIn(minWidth = 400.dp, maxWidth = 600.dp)
                }
            )
            .padding(vertical = 15.dp, horizontal = 30.dp)
            .background(accentColor.color, RoundedCornerShape(15.dp))
            .clickable(
                onClick = {
                    dbh.goTo(targetScr)
                }
            )
            .padding(vertical = 20.dp3(), horizontal = 12.dp3())
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NavBtnText(text)
    }
}


@Composable
private fun NavBtnText(                                                             text: String
) {
    Text(
        text = text,
        color = LibColor.WhiteText.color,
        fontSize = 30.sp2(),
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(7f, 7f),
                blurRadius = 8f
            )
        )
    )
}