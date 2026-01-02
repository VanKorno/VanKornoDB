package com.vankorno.sandbox.H_O_M_E

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbFileNameFromDb
import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbh
import com.vankorno.sandbox._entities.DbVersion
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._navig.ScrPlayground
import com.vankorno.sandbox._navig.goTo
import com.vankorno.sandbox._ui.LargeBtn
import com.vankorno.vankornocompose.composables.Spa_______________cer
import com.vankorno.vankornocompose.dp3
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.sp2
import com.vankorno.vankornocompose.theme_main.LibAccentColor
import com.vankorno.vankornocompose.theme_main.LibColor
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
    LargeBtn(text, accentColor) { dbh.goTo(targetScr) }
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














