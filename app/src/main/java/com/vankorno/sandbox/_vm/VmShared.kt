package com.vankorno.sandbox._vm

import androidx.lifecycle.ViewModel
import com.vankorno.sandbox._menus.popup._data.PopupOFF
import com.vankorno.sandbox._menus.popup._data.PopupState
import com.vankorno.sandbox._navig.ScrHome
import com.vankorno.sandbox._navig.Screen
import com.vankorno.vankornodb.get.raw.data.RawTableStr
import com.vankorno.vankornohelpers.dLog

private const val TAG = "VmShared"

class VmShared : ViewModel() {
    
    val currScreen = VmVal<Screen>(ScrHome)
    val previousScreen = VmVal<Screen>(ScrHome)
    val popupState = VmVal<PopupState>(PopupOFF)
    
    var internalTables = VmVal(emptyList<String>())
    var appTables = VmVal(emptyList<String>())
    
    var currTable = VmVal("")
    
    val rawColumns = VmVal(emptyList<String>())
    val rawTypes = VmVal(emptyList<String>())
    val rawRows = VmVal<List<List<String>>>(emptyList())
    
    fun setRawTableData(                                                         table: RawTableStr
    ) {
        // region LOG
            dLog(TAG, "setRawTableData(): currTable = $currTable, column count: ${rawColumns.value.size}, row count: ${rawRows.value.size}")
        // endregion
        rawColumns.value = table.columns
        rawTypes.value = table.types
        rawRows.value = table.rows
    }
    fun clearRawTableData() {
        // region LOG
            dLog(TAG, "clearRawTableData()")
        // endregion
        rawColumns.value = emptyList()
        rawTypes.value = emptyList()
        rawRows.value = emptyList()
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    init {
        // region LOG
            dLog(TAG, TAG + " created.")
        // endregion
    }
    override fun onCleared() {
        super.onCleared()
        // region LOG
            dLog(TAG, TAG + " cleared!")
        // endregion
    }
}
