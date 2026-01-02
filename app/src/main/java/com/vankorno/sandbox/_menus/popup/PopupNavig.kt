package com.vankorno.sandbox._menus.popup

import androidx.compose.runtime.Composable
import com.vankorno.sandbox._menus.popup.context_info.ContextInfoPopup
import com.vankorno.vankornocompose.navig.PopState
import com.vankorno.vankornocompose.navig.PopStateContextInfo

@Composable
fun PopNavig(                                                                   popState: PopState
) {
    when (popState) {
        PopStateContextInfo -> { ContextInfoPopup() }
        else -> {}
    }
}





