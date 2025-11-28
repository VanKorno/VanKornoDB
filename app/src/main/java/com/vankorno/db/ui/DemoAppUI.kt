package com.vankorno.db.ui

import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintLayout
import com.vankorno.db.ui.scr_home.LayoutsHome
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.values.LocalScreen
import com.vankorno.vankornocompose.values.MOD_MaxH
import com.vankorno.vankornocompose.values.MOD_MaxSize
import com.vankorno.vankornocompose.values.MOD_MaxW


@Composable
fun DemoAppUI() {
    ConstraintLayout {
        val (barTop, topShadow, body, barBottom, popup) = createRefs()
        
        val modifTop = MOD_MaxW
            //.testTag(TTag_TopBar)
            .constrainAs(barTop) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifBody = MOD_MaxH
            .constrainAs(body) {
                top.linkTo(barTop.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifTopShadow = MOD_MaxW
            .constrainAs(topShadow) {
                top.linkTo(barTop.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifBot = MOD_MaxW
            //.testTag(TTag_BotBar)
            .constrainAs(barBottom) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        
        val modifPopup = MOD_MaxSize
            .constrainAs(popup) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        ScrNavig(ScrModifiers(modifTop, modifTopShadow, modifBody, modifBot))
    }
}


@Composable
fun ScrNavig(                                                               bodyModif: ScrModifiers
) {
    when (LocalScreen.current) {
        ScrHome -> LayoutsHome(bodyModif)
        else -> {}
    }
}

