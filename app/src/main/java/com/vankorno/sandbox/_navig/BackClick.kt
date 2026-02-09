package com.vankorno.sandbox._navig

import com.vankorno.vankornocompose.navig.PopupOFF
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.values.LibGlobals2.libVm
import com.vankorno.vankornocompose.values.popupOFF
import com.vankorno.vankornohelpers.dLog
import com.vankorno.vankornohelpers.values.minimizeApp

private const val TAG = "BackClick"

class BackClick() : Navig() {
    
    fun goBack() {
        val currScreen = libVm.currScreen.value
        val previousScreen = libVm.previousScreen.value
        // region LOG
            dLog(TAG, "clickBack()")
        // endregion
        if (libVm.popupState != PopupOFF) {
            popupBackBtn()
            return //\/\/\/\/\/\
        }
        
        // region LOG
            dLog(TAG, "Previous screen: $previousScreen")
        // endregion
        when (currScreen) {
            ScrHome -> minimizeApp()
            ScrPlayground -> { goTo(ScrHome) }
            ScrDbBrowser -> { goTo(ScrHome) }
            else -> goToPrevScr()
        }
    }
    
    private fun popupBackBtn() {
        // region LOG
            dLog(TAG, "popupBackBtn()")
        // endregion
        when (libVm.popupState) {
            
            else -> { popupOFF() }
        }
    }
    
    private fun goToPrevScr() {
        val currScreen = libVm.currScreen.value
        val previousScreen = libVm.previousScreen.value
        // region LOG
            dLog(TAG, "goToPrevScr(currScr = $currScreen, prevScr = $previousScreen)")
        // endregion
        if (currScreen == previousScreen) {
            // region LOG
                dLog(TAG, "currScr == prevScr == $currScreen. Returning to the Main screen...")
            // endregion
            goTo(ScrHome)
            return //\/\/\/\/\/\
        }
        
        goTo(previousScreen)
    }
}