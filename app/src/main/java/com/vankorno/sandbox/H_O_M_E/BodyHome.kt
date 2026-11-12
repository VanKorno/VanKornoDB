package com.vankorno.sandbox.H_O_M_E

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import com.vankorno.sandbox.App.Companion.dbFileNameFromDb
import com.vankorno.sandbox._entities.DbVersion
import com.vankorno.sandbox._entities.EntityMeta
import com.vankorno.sandbox._navig.Navigator.goToScreen
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._navig.ScrPlayground
import com.vankorno.sandbox._navig.Screen
import com.vankorno.sandbox._ui.composables.LargeBtn
import com.vankorno.sandbox._ui.composables.Spa_______________cer
import com.vankorno.sandbox._ui.theme.AccentColor
import com.vankorno.sandbox._ui.theme.AppColor


@Composable
fun BodyHome() {
    NavBtns()
    
    Spa_______________cer()
    
    DbInfoSection()
}


@Composable
private fun NavBtns() {
    NavBtn(ScrPlayground, "Playground", AccentColor.Pink)
    
    NavBtn(ScrDbBrowser, "DB Browser", AccentColor.Green)
}


@Composable
private fun NavBtn(                                                      targetScr: Screen,
                                                                              text: String,
                                                                       accentColor: AccentColor,
) {
    LargeBtn(text, accentColor) { goToScreen(targetScr) }
}





@Composable
private fun DbInfoSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 30.dp) // TODO Merge
            .background(AppColor.Surface.color)
            .padding(vertical = 18.dp, horizontal = 12.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Db Info",
            color = Color.White,
            fontSize = 22.sp,
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
        color = Color.White,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        
    )
}














