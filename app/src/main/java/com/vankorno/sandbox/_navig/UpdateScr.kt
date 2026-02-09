package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.values.LibGlobals2.libVm
import com.vankorno.vankornodb.api.DbHelperHolder.dbh
import com.vankorno.vankornohelpers.dLog

private const val TAG = "UpdateScr"

class UpdateScr() {
    
    fun updateEveryMinute() {
        updateScr()
    }
    
    fun updateScr() {
        val currScreen = libVm.currScreen.value
        // region LOG
            dLog("UpdateScr", "update(). Curr.screen: $currScreen")
        // endregion
        when (currScreen) {
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