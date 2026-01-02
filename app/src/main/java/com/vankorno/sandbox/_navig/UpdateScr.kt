package com.vankorno.sandbox._navig

import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbh
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.values.LibGlobals2.currScr
import com.vankorno.vankornohelpers.dLog

private const val TAG = "UpdateScr"

class UpdateScr() {
    
    fun updateEveryMinute() {
        updateScr()
    }
    
    fun updateScr() {
        // region LOG
            dLog("UpdateScr", "update(). Curr.screen: $currScr")
        // endregion
        when (currScr) {
            ScrHome -> { updateScrMain() }
            ScrPlayground -> { updateReorder() }
            ScrDbBrowser -> { updateDbBrowser() }
            
            else -> {}
        }
    }
    
    
    private fun updateScrMain() {
        // region LOG
            dLog(TAG, "updateScrMain()")
        // endregion
        
    }
    
    
    private fun updateReorder() {
        
    }
    
    private fun updateDbBrowser() {
        vm.appTables = dbh.getAppTableNames()
    }
    
    
    
    
    
    
    
    
    
}