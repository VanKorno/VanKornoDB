package com.vankorno.sandbox.navig

import android.database.sqlite.SQLiteDatabase
import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornocompose.values.LibGlobals2.currScr
import com.vankorno.vankornocompose.values.LibGlobals2.previousScr
import com.vankorno.vankornodb.getSet.getAppTableNames
import com.vankorno.vankornodb.getSet.getInternalTableNames
import com.vankorno.vankornohelpers.dLog
import com.vankorno.vankornohelpers.values.hideKeyboard

private const val TAG = "Navig"


open class Navig(val db: SQLiteDatabase) {
    
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
                vm.internalTables = db.getInternalTableNames()
                vm.appTables = db.getAppTableNames()
            }
            else -> {}
        }
    }
    
    
    private fun runAfterGoing(                                                 targetScr: Screen
    ) {
        when (previousScr) {
            
            else -> {}
        }
        
        when (targetScr) {
            
            else -> {}
        }
    }
}














