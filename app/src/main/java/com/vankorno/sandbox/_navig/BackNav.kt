package com.vankorno.sandbox._navig

import com.vankorno.vankornocompose.navig.BaseBackNavigator
import com.vankorno.vankornocompose.navig.Navig
import com.vankorno.vankornocompose.navig.PopupOFF
import com.vankorno.vankornocompose.navig.PopupState
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.values.LibGlobals2.libVm
import com.vankorno.vankornocompose.values.LibLambdas2.popupOFF
import com.vankorno.vankornohelpers.dLog
import com.vankorno.vankornohelpers.values.LibLambdas.minimizeApp

object BackNav : BaseBackNavigator() {
    
    override fun onBackWithoutPopup(                                          currScreen: Screen,
                                                                          previousScreen: Screen,
    ) {
        when (currScreen) {
            ScrHome -> minimizeApp()
            ScrPlayground -> { Navig.goTo(ScrHome) }
            ScrDbBrowser -> { Navig.goTo(ScrHome) }
            else -> goToPrevScr(currScreen, previousScreen)
        }
    }
    
    override fun onBackFromPopup(                                             popupState: PopupState
    ) {
        when (popupState) {
            
            else -> { popupOFF() }
        }
    }
    
    
    private fun goToPrevScr(                                                  currScreen: Screen,
                                                                          previousScreen: Screen,
    ) {
        // region LOG
            dLog(TAG, "goToPrevScr(currScr = $currScreen, prevScr = $previousScreen)")
        // endregion
        if (currScreen == previousScreen) {
            // region LOG
                dLog(TAG, "currScr == prevScr == $currScreen. Returning to the Main screen...")
            // endregion
            Navig.goTo(ScrHome)
            return //\/\/\/\/\/\
        }
        
        Navig.goTo(previousScreen)
    }
}
