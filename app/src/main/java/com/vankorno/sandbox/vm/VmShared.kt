package com.vankorno.sandbox.vm

import androidx.lifecycle.ViewModel
import com.vankorno.vankornohelpers.dLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "VmShared"

class VmShared : ViewModel() {
    
    private val _internalTables = MutableStateFlow(emptyList<String>())
    val internalTablesFlow: StateFlow<List<String>> = _internalTables
    var internalTables: List<String>
        get() = _internalTables.value
        set(new) { _internalTables.value = new }
    
    
    private val _appTables = MutableStateFlow(emptyList<String>())
    val appTablesFlow: StateFlow<List<String>> = _appTables
    var appTables: List<String>
        get() = _appTables.value
        set(new) { _appTables.value = new }
    
    
    
    private val _currTable = MutableStateFlow("")
    var currTable: String
        get() = _currTable.value
        set(new) { _currTable.value = new }
    
    
    private val _rawColumns = MutableStateFlow<List<String>>(emptyList())
    val rawColumnsFlow: StateFlow<List<String>> = _rawColumns
    var rawColumns: List<String>
        get() = _rawColumns.value
        set(new) { _rawColumns.value = new }
    
    
    private val _rawRows = MutableStateFlow<List<List<String>>>(emptyList())
    val rawRowsFlow: StateFlow<List<List<String>>> = _rawRows
    var rawRows: List<List<String>>
        get() = _rawRows.value
        set(new) { _rawRows.value = new }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
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