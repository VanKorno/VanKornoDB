package com.vankorno.sandbox._menus.popup

import androidx.compose.runtime.Composable
import com.vankorno.sandbox._menus.popup._data.PopupContextInfo
import com.vankorno.sandbox._menus.popup._data.PopupState
import com.vankorno.sandbox._menus.popup.context_info.ContextInfoPopup

@Composable
fun PopNavig(                                                                   popState: PopupState
) {
    when (popState) {
        PopupContextInfo -> { ContextInfoPopup() }
        else -> {}
    }
}





