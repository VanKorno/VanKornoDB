package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornohelpers.values.LibGlobals.updatingScreen

object ScreenUpdater {
    
    fun updateScr() {
        updatingScreen = true
        val currScreen = vm.currScreen.value
        
        onUpdateScreen(currScreen)
        updatingScreen = false
    }
    
    fun onUpdateScreen(
        currScreen: Screen
    ) {
        when (currScreen) {
            ScrHome -> { updateScrMain() }
            ScrPlayground -> { updateReorder() }
            ScrDbBrowser -> { updateDbBrowser() }
            else -> {}
        }
    }
    
    private fun updateScrMain() {
        
    }
    
    private fun updateReorder() {}
    
    private fun updateDbBrowser() {
        vm.appTables.value = dbh.getAppTableNames()
    }
    
    
    
    
    
    
}
