package com.vankorno.sandbox._navig

import com.vankorno.sandbox.MainActivity.Companion.vm
import com.vankorno.sandbox._navig.Navigator.goToScreen
import com.vankorno.sandbox._navig.Navigator.onGoToStart
import com.vankorno.vankornohelpers.values.LibGlobals.navigatingBack
import com.vankorno.vankornohelpers.values.LibLambdas.minimizeApp

object BackNav {
    
    fun goBack() {
        navigatingBack = true
        
        val currScreen = vm.currScreen.value
        val previousScreen = vm.previousScreen.value
        
        when (currScreen) {
            ScrHome -> minimizeApp()
            ScrPlayground -> { onGoToStart() }
            ScrDbBrowser -> { onGoToStart() }
            else -> goToPrevScr(currScreen, previousScreen)
        }
        navigatingBack = false
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
