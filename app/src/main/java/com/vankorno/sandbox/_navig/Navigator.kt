package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornohelpers.values.LibGlobals.navigating
import com.vankorno.vankornohelpers.values.LibLambdas.hideKeyboard

object Navigator {
    
    fun goToScreen(                                                   targetScreen: Screen,
                                                                  autoHideKeyboard: Boolean = true,
    ) {
        navigating = true
        
        if (autoHideKeyboard)
            hideKeyboard()
        
        val previousScreen = vm.currScreen.value
        vm.previousScreen.value = previousScreen
        
        runBeforeGoing(previousScreen, targetScreen)
        
        vm.currScreen.value = targetScreen
        
        runAfterGoing(previousScreen, targetScreen)
        
        navigating = false
    }
    
    fun onGoToStart() {
        goToScreen(ScrHome)
    }
    
    fun runBeforeGoing(                                                   previousScreen: Screen,
                                                                            targetScreen: Screen,
    ) {
        when (targetScreen) {
            ScrDbBrowser -> {
                vm.internalTables.value = dbh.getInternalTableNames()
                vm.appTables.value = dbh.getAppTableNames()
            }
            ScrTable -> {
                vm.setRawTableData(dbh.getRawTableStr(vm.currTable.value))
            }
            else -> {}
        }
    }
    
    fun runAfterGoing(                                                    previousScreen: Screen,
                                                                            targetScreen: Screen,
    ) {
        when (previousScreen) {
            ScrDbBrowser -> {
                vm.internalTables.reset()
                vm.appTables.reset()
            }
            ScrTable -> {
                vm.clearRawTableData()
            }
            else -> {}
        }
    }
}
