package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.BaseScreenUpdater
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.values.LibGlobals2.libVm
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornohelpers.dLog

object ScreenUpdater : BaseScreenUpdater() {
    
    override fun onUpdateScreen(                                              currScreen: Screen
    ) {
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
        vm.appTables.value = dbh.getAppTableNames()
    }
    
    
    
    
    
    
    
    
    
}
