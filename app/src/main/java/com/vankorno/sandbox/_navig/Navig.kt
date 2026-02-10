package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.values.LibGlobals2.libVm
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornohelpers.dLog
import com.vankorno.vankornohelpers.values.hideKeyboard

private const val TAG = "Navig"


open class Navig() {
    
    fun goTo(                                                           targetScr: Screen = ScrHome
    ) {
        // region LOG
            dLog(TAG, "goTo(targetScr = ${targetScr})")
        // endregion
        hideKeyboard()
        libVm.previousScreen.value = libVm.currScreen.value
        
        specialBackstack(targetScr)
        
        runBeforeGoing(targetScr)
        
        libVm.currScreen.value = targetScr // GO
        
        runAfterGoing(targetScr)
    }
    
    
    private fun specialBackstack(                                              targetScr: Screen
    ) {
        // region LOG
            dLog(TAG, "specialBackstack(previousScr = ${libVm.previousScreen.value}, targetScr = $targetScr)")
        // endregion
        
    }
    
    
    private fun runBeforeGoing(                                                targetScr: Screen
    ) {
        val previousScreen = libVm.previousScreen.value
        
        when (previousScreen) {
            
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
        val previousScreen = libVm.previousScreen.value
        
        when (previousScreen) {
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














