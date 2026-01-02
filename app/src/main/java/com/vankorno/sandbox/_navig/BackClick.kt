package com.vankorno.sandbox._navig

import com.vankorno.vankornocompose.LibMainActivity.Companion.libVm
import com.vankorno.vankornocompose.navig.PopStateOFF
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.values.LibGlobals2.currScr
import com.vankorno.vankornocompose.values.LibGlobals2.previousScr
import com.vankorno.vankornocompose.values.popupOFF
import com.vankorno.vankornohelpers.dLog
import com.vankorno.vankornohelpers.values.minimizeApp

private const val TAG = "BackClick"

class BackClick() : Navig() {
    
    fun goBack() {
        // region LOG
            dLog(TAG, "clickBack()")
        // endregion
        if (libVm.popState != PopStateOFF) {
            popupBackBtn()
            return //\/\/\/\/\/\
        }
        
        // region LOG
            dLog(TAG, "Previous screen: $previousScr")
        // endregion
        when (currScr) {
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
        when (libVm.popState) {            
            
            else -> { popupOFF() }
        }
    }
    
    private fun goToPrevScr() {
        // region LOG
            dLog(TAG, "goToPrevScr(currScr = $currScr, prevScr = $previousScr)")
        // endregion
        if (currScr == previousScr) {
            // region LOG
                dLog(TAG, "currScr == prevScr == $currScr. Returning to the Main screen...")
            // endregion
            goTo(ScrHome)
            return //\/\/\/\/\/\
        }
        
        goTo(previousScr)
    }
}