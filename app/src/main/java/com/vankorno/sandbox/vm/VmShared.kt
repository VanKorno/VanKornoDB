package com.vankorno.sandbox.vm

import androidx.lifecycle.ViewModel
import com.vankorno.vankornohelpers.dLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "VmShared"

class VmShared : ViewModel() {
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
    
    
    
    
    
}