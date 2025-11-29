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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vankorno.sandbox.DbVersion
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.dbFileNameFromDb
import com.vankorno.sandbox.entities.EntityMeta
import com.vankorno.sandbox.navig.ScrDbBrowser
import com.vankorno.sandbox.navig.ScrPlayground
import com.vankorno.sandbox.navig.goTo
import com.vankorno.vankornocompose.actions.applyIf
import com.vankorno.vankornocompose.composables.Spa_______________cer
import com.vankorno.vankornocompose.dp3
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.sp2
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.LocalScrType
import com.vankorno.vankornocompose.values.MOD_MaxW


@Composable
fun BodyHome() {
    NavBtns()
    
    Spa_______________cer()
    
    DbInfoSection()
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



@Composable
private fun DbInfoSection() {
    Column(
        MOD_MaxW
            .padding(vertical = 15.dp, horizontal = 30.dp) // TODO Merge
            .background(LibColor.Surface.color)
            .padding(vertical = 16.dp3(), horizontal = 10.dp3())
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Db Info",
            color = LibColor.WhiteText.color,
            fontSize = 20.sp2(),
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
        Spa_______________cer(20)
        
        InfoText("File name:    " + dbFileNameFromDb)
        
        InfoText("Version:    " + DbVersion)
        
        InfoText("App entities:    " + EntityMeta.entries.size)
        
        InfoText("To be continued...")
        
    }
}

@Composable
private fun InfoText(                                                               text: String
) {
    Text(
        modifier = Modifier.padding(bottom = 7.dp),
        text = text,
        color = LibColor.WhiteText.color,
        fontSize = 16.sp2(),
        textAlign = TextAlign.Center,
        
    )
}














