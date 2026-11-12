package com.vankorno.sandbox.AppStart._ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.vankorno.sandbox.AppStart._data.LayoutModifiers
import com.vankorno.sandbox.DbBrowser.LayoutsDbBrowser
import com.vankorno.sandbox.DbBrowser.TableBrowser._ui.LayoutsTable
import com.vankorno.sandbox.H_O_M_E.LayoutsHome
import com.vankorno.sandbox.Playground.LayoutsPlayground
import com.vankorno.sandbox._navig.ScrDbBrowser
import com.vankorno.sandbox._navig.ScrHome
import com.vankorno.sandbox._navig.ScrPlayground
import com.vankorno.sandbox._navig.ScrTable
import com.vankorno.sandbox._ui.LocalScreen


@Composable
fun DemoAppUI() {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (barTop, topShadow, body, barBottom) = createRefs()
        
        val modifTop = Modifier.fillMaxWidth()
            .constrainAs(barTop) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifBody = Modifier.fillMaxHeight()
            .constrainAs(body) {
                top.linkTo(barTop.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifTopShadow = Modifier.fillMaxWidth()
            .constrainAs(topShadow) {
                top.linkTo(barTop.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifBot = Modifier.fillMaxWidth()
            .constrainAs(barBottom) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        
        val modifiers = LayoutModifiers(modifBody, modifTop, modifTopShadow, modifBot)
        
        ScrNavig(modifiers)
    }
}


@Composable
fun ScrNavig(                                                          bodyModif: LayoutModifiers
) {
    when (LocalScreen.current) {
        ScrHome -> LayoutsHome(bodyModif)
        ScrPlayground -> LayoutsPlayground(bodyModif)
        ScrDbBrowser -> LayoutsDbBrowser(bodyModif)
        ScrTable -> LayoutsTable(bodyModif)
        
        
        else -> {}
    }
}

