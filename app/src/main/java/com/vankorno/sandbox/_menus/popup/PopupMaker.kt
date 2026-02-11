package com.vankorno.sandbox._menus.popup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vankorno.vankornocompose.composables.LibPopup
import com.vankorno.vankornocompose.navig.PopupState
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.LocalScrType

@Composable
fun PopupMaker(                                                               popState: PopupState,
                                                                              modifier: Modifier,
) {
    LibPopup(modifier, LocalScrType.current,
        scrimColor = LibColor.BlackScrim.color,
        cardColor = LibColor.Surface.color,
    ) {
        PopNavig(popState)
    }
}





