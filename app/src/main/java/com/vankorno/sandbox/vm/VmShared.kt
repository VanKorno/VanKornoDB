package com.vankorno.sandbox.vm

import androidx.lifecycle.ViewModel
import com.vankorno.vankornodb.getSet.raw.data.RawTableStr
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
    
    private val _rawTypes = MutableStateFlow<List<String>>(emptyList())
    val rawTypesFlow: StateFlow<List<String>> = _rawTypes
    
    private val _rawRows = MutableStateFlow<List<List<String>>>(emptyList())
    val rawRowsFlow: StateFlow<List<List<String>>> = _rawRows
    
    fun setRawTableData(                                                         table: RawTableStr
    ) {
        // region LOG
            dLog(TAG, "setRawTableData(): currTable = $currTable, column count: ${_rawColumns.value.size}, row count: ${_rawRows.value.size}")
        // endregion
        _rawColumns.value = table.columns
        _rawTypes.value = table.types
        _rawRows.value = table.rows
    }
    fun clearRawTableData() {
        // region LOG
            dLog(TAG, "clearRawTableData()")
        // endregion
        _rawColumns.value = emptyList()
        _rawTypes.value = emptyList()
        _rawRows.value = emptyList()
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