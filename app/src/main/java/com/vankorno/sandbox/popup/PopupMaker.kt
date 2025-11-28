package com.vankorno.sandbox.popup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vankorno.vankornocompose.composables.LibPopup
import com.vankorno.vankornocompose.navig.PopState
import com.vankorno.vankornocompose.theme_main.LibColor
import com.vankorno.vankornocompose.values.LocalScrType
import com.vankorno.vankornocompose.values.goBack

@Composable
fun PopupMaker(                                                                 popState: PopState,
                                                                                modifier: Modifier,
) {
    LibPopup(modifier, LocalScrType.current,
        scrimColor = LibColor.BlackScrim.color,
        cardColor = LibColor.Surface.color,
        clickScrim = goBack
    ) {
        PopNavig(popState)
    }
}





