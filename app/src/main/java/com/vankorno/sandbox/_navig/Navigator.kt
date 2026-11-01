package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.BaseNavigator
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.values.LibGlobals2.libVm
import com.vankorno.vankornocompose.vm.reset
import com.vankorno.vankornodb.api.DbRuntime.dbh

object Navigator : BaseNavigator() {
    
    override fun runBeforeGoing(                                          previousScreen: Screen,
                                                                            targetScreen: Screen,
    ) {
        /*when (previousScreen) {
            
            else -> {}
        }*/
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
    
    
    override fun runAfterGoing(                                           previousScreen: Screen,
                                                                            targetScreen: Screen,
    ) {
        when (previousScreen) {
            ScrDbBrowser -> {
                vm.internalTables.reset()
                vm.appTables.reset()
            }
            ScrTable -> { vm.clearRawTableData() }
            else -> {}
        }
        
        /*when (targetScreen) {
            
            else -> {}
        }*/
    }
}














