package com.vankorno.sandbox.AppStart._ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vankorno.sandbox.DbBrowser.LayoutsDbBrowser
import com.vankorno.sandbox.DbBrowser.TableBrowser._ui.LayoutsTable
import com.vankorno.sandbox.H_O_M_E.LayoutsHome
import com.vankorno.sandbox.Playground.LayoutsPlayground
import com.vankorno.sandbox._menus.popup.PopupMaker
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._navig.ScrPlayground
import com.vankorno.sandbox._navig.ScrTable
import com.vankorno.vankornocompose.LibMainActivity.Companion.libVm
import com.vankorno.vankornocompose.navig.PopStateOFF
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.values.LibLayoutModifiers
import com.vankorno.vankornocompose.values.LocalScreen
import com.vankorno.vankornocompose.values.MOD_MaxH
import com.vankorno.vankornocompose.values.MOD_MaxSize
import com.vankorno.vankornocompose.values.MOD_MaxW


@Composable
fun DemoAppUI() {
    ConstraintLayout(MOD_MaxSize) {
        val (barTop, topShadow, body, barBottom, popup) = createRefs()
        
        val popState by libVm.popStateFlow.collectAsStateWithLifecycle()
        
        val modifTop = MOD_MaxW
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
        
        val modifiers = LibLayoutModifiers(modifBody, modifTop, modifTopShadow, modifBot, modifPopup)
        
        ScrNavig(modifiers)
        
        AnimatedVisibility(popState != PopStateOFF) {
            PopupMaker(popState, modifPopup)
        }
    }
}


@Composable
fun ScrNavig(                                                          bodyModif: LibLayoutModifiers
) {
    when (LocalScreen.current) {
        ScrHome -> LayoutsHome(bodyModif)
        ScrPlayground -> LayoutsPlayground(bodyModif)
        ScrDbBrowser -> LayoutsDbBrowser(bodyModif)
        ScrTable -> LayoutsTable(bodyModif)
        
        
        else -> {}
    }
}

