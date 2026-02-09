package com.vankorno.sandbox._menus.popup

import androidx.compose.runtime.Composable
import com.vankorno.sandbox._menus.popup.context_info.ContextInfoPopup
import com.vankorno.vankornocompose.navig.PopupContextInfo
import com.vankorno.vankornocompose.navig.PopupState

@Composable
fun PopNavig(                                                                   popState: PopupState
) {
    when (popState) {
        PopupContextInfo -> { ContextInfoPopup() }
        else -> {}
    }
}





