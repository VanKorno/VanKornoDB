package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.sandbox._menus.popup._data.PopupOFF
import com.vankorno.sandbox._menus.popup._data.PopupState
import com.vankorno.sandbox._navig.Navigator.goToScreen
import com.vankorno.sandbox._navig.Navigator.onGoToStart
import com.vankorno.vankornohelpers.values.LibGlobals.navigatingBack
import com.vankorno.vankornohelpers.values.LibLambdas.minimizeApp

object BackNav {
    
    fun onGoBack() {
        navigatingBack = true
        
        if (vm.popupState.value != PopupOFF) {
            popup(vm.popupState.value)
        } else {
            onBackWithoutPopup(vm.currScreen.value, vm.previousScreen.value)
        }
        navigatingBack = false
    }
    
    private fun popup(popupState: PopupState) {
        onBackFromPopup(popupState)
    }
    
    fun onBackWithoutPopup(
        currScreen: Screen,
        previousScreen: Screen,
    ) {
        when (currScreen) {
            ScrHome -> minimizeApp()
            ScrPlayground -> { onGoToStart() }
            ScrDbBrowser -> { onGoToStart() }
            else -> goToPrevScr(currScreen, previousScreen)
        }
    }
    
    fun onBackFromPopup(popupState: PopupState) {
        when (popupState) {
            else -> {
                vm.popupState.value = PopupOFF
            }
        }
    }
    
    
    private fun goToPrevScr(                                                  currScreen: Screen,
                                                                          previousScreen: Screen,
    ) {
        if (currScreen == previousScreen) {
            onGoToStart()
            return //\/\/\/\/\/\
        }
        
        goToScreen(previousScreen)
    }
}
