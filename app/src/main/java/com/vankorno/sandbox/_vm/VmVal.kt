package com.vankorno.sandbox._vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class VmVal<T>(private val initialValue: T) {
    private val state: MutableState<T> = mutableStateOf(initialValue)
    
    var value: T
        get() = state.value
        set(value) {
            state.value = value
        }
    
    fun state(): State<T> = state
    
    fun reset() {
        state.value = initialValue
    }
}
