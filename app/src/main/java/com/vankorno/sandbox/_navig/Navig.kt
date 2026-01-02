package com.vankorno.sandbox._navig

import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbh
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.values.LibGlobals2.currScr
import com.vankorno.vankornocompose.values.LibGlobals2.previousScr
import com.vankorno.vankornohelpers.dLog
import com.vankorno.vankornohelpers.values.hideKeyboard

private const val TAG = "Navig"


open class Navig() {
    
    fun goTo(                                                           targetScr: Screen = ScrHome
    ) {
        // region LOG
            dLog(TAG, "goTo(targetScr = ${targetScr.name})")
        // endregion
        hideKeyboard()
        previousScr = currScr
        
        specialBackstack(targetScr)
        
        runBeforeGoing(targetScr)
        
        currScr = targetScr // GO
        
        runAfterGoing(targetScr)
    }
    
    
    private fun specialBackstack(                                              targetScr: Screen
    ) {
        // region LOG
                dLog(TAG, "specialBackstack(previousScr = ${previousScr.name}, targetScr = ${targetScr.name})")
            // endregion
        
    }
    
    
    private fun runBeforeGoing(                                                targetScr: Screen
    ) {
        when (previousScr) {
            
            else -> {}
        }
        when (targetScr) {
            ScrDbBrowser -> {
                vm.internalTables = dbh.getInternalTableNames()
                vm.appTables = dbh.getAppTableNames()
            }
            ScrTable -> {
                vm.setRawTableData(dbh.getRawTableStr(vm.currTable))
            }
            
            else -> {}
        }
    }
    
    
    private fun runAfterGoing(                                                 targetScr: Screen
    ) {
        when (previousScr) {
            ScrDbBrowser -> {
                vm.internalTables = emptyList()
                vm.appTables = emptyList()
            }
            ScrTable -> { vm.clearRawTableData() }
            else -> {}
        }
        
        when (targetScr) {
            
            else -> {}
        }
    }
}














